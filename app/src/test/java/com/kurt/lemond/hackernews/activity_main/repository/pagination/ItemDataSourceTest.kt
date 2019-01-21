package com.kurt.lemond.hackernews.activity_main.repository.pagination

import androidx.paging.PositionalDataSource
import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.pagination.factory.*
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`


class ItemDataSourceTest {

    private val totalIdCount = 100
    private val initialLoadSize = 10
    private val pageSize = 5
    private val startPosition = 0

    private val loadInitialParams = PositionalDataSource.LoadInitialParams(startPosition, initialLoadSize, pageSize, true)

    @Mock
    private lateinit var loadInitialCallback: PositionalDataSource.LoadInitialCallback<DataWrapper<User>>

    @Mock
    private lateinit var loadRangeCallback: PositionalDataSource.LoadRangeCallback<DataWrapper<User>>

    @Mock
    private lateinit var userDataRepository: DataRepository<User>

    private lateinit var disposableCollector: CompositeDisposable

    @Before
    fun setup() {
        loadInitialCallback = mock()
        loadRangeCallback = mock()
        userDataRepository = mock()
        disposableCollector = CompositeDisposable()
    }

    @After
    fun tearDown() {
        disposableCollector.clear()
    }

    @Test
    fun failed_to_load_all_ids_on_load_initial() {
        val dataWrappedError = createErrorDataWrapper<List<Long>>()

        `when`(userDataRepository.getIds()).thenReturn(Single.just(dataWrappedError))

        val itemDataSource = ItemDataSource(userDataRepository, disposableCollector)

        itemDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        Mockito.verify(userDataRepository).getIds()
        Mockito.verify(loadInitialCallback).onResult(mutableListOf(), 0, 0)
    }

    @Test
    fun failed_to_load_due_to_no_network_all_ids_on_load_initial() {
        val dataWrappedError = createNoNetworkDataWrapper<List<Long>>()

        `when`(userDataRepository.getIds()).thenReturn(Single.just(dataWrappedError))

        val itemDataSource = ItemDataSource(userDataRepository, disposableCollector)

        itemDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        Mockito.verify(userDataRepository).getIds()
        Mockito.verify(loadInitialCallback).onResult(mutableListOf(), 0, 0)
    }

    @Test
    fun loaded_all_ids_on_load_initial() {
        val idList = createRandomLongList(totalIdCount)
        val dataWrappedIdList = createSuccessDataWrapper(idList)

        `when`(userDataRepository.getIds()).thenReturn(Single.just(dataWrappedIdList))

        val itemDataSource = ItemDataSource(userDataRepository, disposableCollector)

        testInitialLoad(idList, itemDataSource)
    }

    @Test
    fun loaded_all_ids_on_load_initial_then_continue_loading_by_page() {
        val idList = createRandomLongList(totalIdCount)
        val dataWrappedIdList = createSuccessDataWrapper(idList)

        `when`(userDataRepository.getIds()).thenReturn(Single.just(dataWrappedIdList))

        val itemDataSource = ItemDataSource(userDataRepository, disposableCollector)

        testInitialLoad(idList, itemDataSource)

        var startPosition = 10
        while (startPosition < totalIdCount) {
            testLoadByStartPosition(startPosition, idList, itemDataSource)
            startPosition += 5
        }
    }

    @Test
    fun loaded_all_ids_on_load_initial_then_continue_loading_by_page_even_with_random_errors() {
        val idList = createRandomLongList(totalIdCount)
        val dataWrappedIdList = createSuccessDataWrapper(idList)

        `when`(userDataRepository.getIds()).thenReturn(Single.just(dataWrappedIdList))

        val itemDataSource = ItemDataSource(userDataRepository, disposableCollector)

        testInitialLoad(idList, itemDataSource)

        var startPosition = 10
        while (startPosition < totalIdCount) {
            testLoadByStartPositionWithRandomErrors(startPosition, idList, itemDataSource)
            startPosition += 5
        }
    }

    @Test
    fun loaded_all_ids_on_load_initial_then_continue_loading_by_page_even_with_random_network_errors() {
        val idList = createRandomLongList(totalIdCount)
        val dataWrappedIdList = createSuccessDataWrapper(idList)

        `when`(userDataRepository.getIds()).thenReturn(Single.just(dataWrappedIdList))

        val itemDataSource = ItemDataSource(userDataRepository, disposableCollector)

        testInitialLoad(idList, itemDataSource)

        var startPosition = 10
        while (startPosition < totalIdCount) {
            testLoadByStartPositionWithRandomNetworkErrors(startPosition, idList, itemDataSource)
            startPosition += 5
        }
    }

    private fun testInitialLoad(idList: List<Long>, itemDataSource: ItemDataSource<User>) {
        val userList = ArrayList<DataWrapper<User>>()
        for (i in 0 until initialLoadSize) {
            val randomUser = createRandomUser(idList[i])
            val dataWrappedUser = createSuccessDataWrapper(randomUser)
            userList.add(dataWrappedUser)

            `when`(userDataRepository.getDetails(idList[i])).thenReturn(Single.just(dataWrappedUser))
        }

        itemDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        Mockito.verify(userDataRepository).getIds()
        Mockito.verify(loadInitialCallback).onResult(userList, 0, totalIdCount)
        for (i in 0 until initialLoadSize) {
            Mockito.verify(userDataRepository).getDetails(idList[i])
        }
    }

    private fun testLoadByStartPosition(startPosition: Int, idList: List<Long>, itemDataSource: ItemDataSource<User>) {
        val userList = ArrayList<DataWrapper<User>>()
        for (i in startPosition until (startPosition + pageSize)) {
            val randomUser = createRandomUser(idList[i])
            val dataWrappedUser = createSuccessDataWrapper(randomUser)
            userList.add(dataWrappedUser)

            `when`(userDataRepository.getDetails(idList[i])).thenReturn(Single.just(dataWrappedUser))
        }

        val loadRangeParams = PositionalDataSource.LoadRangeParams(startPosition, pageSize)
        itemDataSource.loadRange(loadRangeParams, loadRangeCallback)

        Mockito.verify(loadRangeCallback).onResult(userList)
        for (i in startPosition until (startPosition + pageSize)) {
            Mockito.verify(userDataRepository).getDetails(idList[i])
        }
    }

    private fun testLoadByStartPositionWithRandomErrors(startPosition: Int, idList: List<Long>, itemDataSource: ItemDataSource<User>) {
        val userList = ArrayList<DataWrapper<User>>()
        for (i in startPosition until (startPosition + pageSize)) {
            val dataWrappedUser: DataWrapper<User>
            dataWrappedUser = if (createRandomBoolean()) {
                val randomUser = createRandomUser(idList[i])
                createSuccessDataWrapper(randomUser)
            } else {
                createErrorDataWrapper()
            }
            userList.add(dataWrappedUser)
            `when`(userDataRepository.getDetails(idList[i])).thenReturn(Single.just(dataWrappedUser))
        }

        val loadRangeParams = PositionalDataSource.LoadRangeParams(startPosition, pageSize)
        itemDataSource.loadRange(loadRangeParams, loadRangeCallback)

        Mockito.verify(loadRangeCallback).onResult(userList)
        for (i in startPosition until (startPosition + pageSize)) {
            Mockito.verify(userDataRepository).getDetails(idList[i])
        }
    }

    private fun testLoadByStartPositionWithRandomNetworkErrors(startPosition: Int, idList: List<Long>, itemDataSource: ItemDataSource<User>) {
        val userList = ArrayList<DataWrapper<User>>()
        for (i in startPosition until (startPosition + pageSize)) {
            val dataWrappedUser: DataWrapper<User>
            dataWrappedUser = if (createRandomBoolean()) {
                val randomUser = createRandomUser(idList[i])
                createSuccessDataWrapper(randomUser)
            } else {
                createNoNetworkDataWrapper()
            }
            userList.add(dataWrappedUser)
            `when`(userDataRepository.getDetails(idList[i])).thenReturn(Single.just(dataWrappedUser))
        }

        val loadRangeParams = PositionalDataSource.LoadRangeParams(startPosition, pageSize)
        itemDataSource.loadRange(loadRangeParams, loadRangeCallback)

        Mockito.verify(loadRangeCallback).onResult(userList)
        for (i in startPosition until (startPosition + pageSize)) {
            Mockito.verify(userDataRepository).getDetails(idList[i])
        }
    }

}