package com.kurt.lemond.hackernews.activity_main.repository.pagination

import com.kurt.lemond.hackernews.activity_main.factory.*
import com.kurt.lemond.hackernews.activity_main.repository.DetailsDataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.io.IOException


class DetailsDataRepositoryTest {

    private val totalIdCount = 50

    @Mock
    private lateinit var detailsDataRepository: DetailsDataRepository<*>

    @Before
    fun setup() {
        detailsDataRepository = Mockito.mock(DetailsDataRepository::class.java, Mockito.CALLS_REAL_METHODS)
    }

    @Test
    fun getIds_success() {
        val randomIdList = createRandomLongList(totalIdCount)
        Mockito.`when`(detailsDataRepository.loadIds()).thenReturn(Single.just(randomIdList))

        val testObserver = detailsDataRepository.getIds().test()

        testObserver.assertSubscribed()
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            return@assertValue it.data == randomIdList && it.getState() ==
                    DataWrapper.State.SUCCESS && it.errorMessage == null
        }
    }

    @Test
    fun getIds_failed() {
        val errorMessage = createRandomString()
        Mockito.`when`(detailsDataRepository.loadIds()).thenReturn(Single.error(IOException(errorMessage)))

        val testObserver = detailsDataRepository.getIds().test()

        testObserver.assertSubscribed()
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            return@assertValue it.data == null && it.getState() ==
                    DataWrapper.State.ERROR && it.errorMessage == errorMessage
        }
    }

    @Test
    fun getIds_success_then_getDetails_all_success() {
        val randomIdList = createRandomLongList(totalIdCount)
        Mockito.`when`(detailsDataRepository.loadIds()).thenReturn(Single.just(randomIdList))

        val randomUserList = ArrayList<User>()
        for(i in 0 until totalIdCount) {
            val randomUser = createRandomUser(randomIdList[i])
            randomUserList.add(randomUser)
            Mockito.`when`(detailsDataRepository.loadDetails(randomIdList[i])).thenReturn(Single.just(randomUser))
        }

        val testObserver = detailsDataRepository.getIds().test()

        testObserver.assertSubscribed()
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            return@assertValue it.data == randomIdList && it.getState() ==
                    DataWrapper.State.SUCCESS && it.errorMessage == null
        }

        val detailsSinglesList = ArrayList<Single<out DataWrapper<out Any?>>>()

        for(i in 0 until totalIdCount) {
            val detailsSingle = detailsDataRepository.getDetails(randomIdList[i])
            detailsSinglesList.add(detailsSingle)
        }

        val testObserver2 = Single.concat(detailsSinglesList).test()

        testObserver2.assertSubscribed()
        testObserver2.assertValueCount(totalIdCount)
        testObserver2.assertComplete()
        for(i in 0 until totalIdCount) {
            testObserver2.assertValueAt(i) {
                return@assertValueAt it.data == randomUserList[i] && it.getState() ==
                        DataWrapper.State.SUCCESS && it.errorMessage == null
            }
        }
    }

    @Test
    fun getIds_success_then_getDetails_all_failed() {
        val randomIdList = createRandomLongList(totalIdCount)
        Mockito.`when`(detailsDataRepository.loadIds()).thenReturn(Single.just(randomIdList))

        val errorMessages = ArrayList<String>()
        for(i in 0 until totalIdCount) {
            val errorMessage = createRandomString()
            errorMessages.add(errorMessage)

            Mockito.`when`(detailsDataRepository.loadDetails(randomIdList[i])).thenReturn(Single.error(RuntimeException(errorMessage)))
        }

        val testObserver = detailsDataRepository.getIds().test()

        testObserver.assertSubscribed()
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            return@assertValue it.data == randomIdList && it.getState() ==
                    DataWrapper.State.SUCCESS && it.errorMessage == null
        }

        val detailsSinglesList = ArrayList<Single<out DataWrapper<out Any?>>>()

        for(i in 0 until totalIdCount) {
            val detailsSingle = detailsDataRepository.getDetails(randomIdList[i])
            detailsSinglesList.add(detailsSingle)
        }

        val testObserver2 = Single.concat(detailsSinglesList).test()

        testObserver2.assertSubscribed()
        testObserver2.assertValueCount(totalIdCount)
        testObserver2.assertComplete()
        for(i in 0 until totalIdCount) {
            testObserver2.assertValueAt(i) {
                return@assertValueAt it.data == null && it.getState() ==
                        DataWrapper.State.ERROR && it.errorMessage == errorMessages[i]
            }
        }
    }

    @Test
    fun getIds_success_then_getDetails_mix_success_failed() {
        val randomIdList = createRandomLongList(totalIdCount)
        Mockito.`when`(detailsDataRepository.loadIds()).thenReturn(Single.just(randomIdList))

        val mockResultList = ArrayList<Any>()
        for(i in 0 until totalIdCount) {

            val mockSingleResult: Single<User>

            mockSingleResult = if (createRandomBoolean()){
                val user = createRandomUser(randomIdList[i])
                mockResultList.add(user)
                Single.just(user)

            } else {
                val errorMessage = createRandomString()
                mockResultList.add(errorMessage)
                Single.error(RuntimeException(errorMessage))
            }

            Mockito.`when`(detailsDataRepository.loadDetails(randomIdList[i])).thenReturn(mockSingleResult)
        }

        val testObserver = detailsDataRepository.getIds().test()

        testObserver.assertSubscribed()
        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue {
            return@assertValue it.data == randomIdList && it.getState() ==
                    DataWrapper.State.SUCCESS && it.errorMessage == null
        }

        val detailsSinglesList = ArrayList<Single<out DataWrapper<out Any?>>>()

        for(i in 0 until totalIdCount) {
            val detailsSingle = detailsDataRepository.getDetails(randomIdList[i])
            detailsSinglesList.add(detailsSingle)
        }

        val testObserver2 = Single.concat(detailsSinglesList).test()

        testObserver2.assertSubscribed()
        testObserver2.assertValueCount(totalIdCount)
        testObserver2.assertComplete()
        for(i in 0 until totalIdCount) {
            if (mockResultList[i] is String) {
                testObserver2.assertValueAt(i) {
                    return@assertValueAt it.data == null && it.getState() ==
                            DataWrapper.State.ERROR && it.errorMessage == mockResultList[i]
                }
            } else if (mockResultList[i] is User) {
                testObserver2.assertValueAt(i) {
                    return@assertValueAt it.data == mockResultList[i] && it.getState() ==
                            DataWrapper.State.SUCCESS && it.errorMessage == null
                }
            }
        }

    }

}