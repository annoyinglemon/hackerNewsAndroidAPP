package com.kurt.lemond.hackernews.activity_main.repository

import androidx.paging.PositionalDataSource
import com.kurt.lemond.hackernews.activity_main.repository.model.DataWrapper
import com.kurt.lemond.hackernews.activity_main.repository.pagination.ItemDataSource
import com.kurt.lemond.hackernews.activity_main.factory.*
import com.nhaarman.mockitokotlin2.*
import io.reactivex.disposables.CompositeDisposable
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito


class ItemDataSourceTest {

    private val initialLoadSize = 10
    private val pageSize = 5
    private val startPosition = 0

    private val loadInitialParams = PositionalDataSource.LoadInitialParams(startPosition, initialLoadSize, pageSize, true)

    @Mock
    private lateinit var loadInitialCallback: PositionalDataSource.LoadInitialCallback<DataWrapper<User>>

    @Mock
    private lateinit var loadRangeCallback: PositionalDataSource.LoadRangeCallback<DataWrapper<User>>

    private lateinit var disposableCollector: CompositeDisposable

    @Before
    fun setup() {
        loadInitialCallback = mock()
        loadRangeCallback = mock()
        disposableCollector = CompositeDisposable()
    }

    @After
    fun tearDown() {
        disposableCollector.clear()
    }

    @Test
    fun failed_to_load_all_ids_on_load_initial() {
        val userDataReposFailed = UserDataRepos_Both_Failed()
        val itemDataSource = ItemDataSource(userDataReposFailed, disposableCollector)

        itemDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        Mockito.verify(loadInitialCallback).onResult(mutableListOf(), 0, 0)
    }

    @Test
    fun failed_to_load_due_to_no_network_all_ids_on_load_initial() {
        val userDataReposNoNetwork = UserDataRepos_Both_NoNetwork()

        val itemDataSource = ItemDataSource(userDataReposNoNetwork, disposableCollector)

        itemDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        Mockito.verify(loadInitialCallback).onResult(mutableListOf(), 0, 0)
    }

    @Test
    fun loaded_all_ids_on_load_initial() {
        val userDataRepo = UserDataRepo_Both_Success()

        val itemDataSource = ItemDataSource(userDataRepo, disposableCollector)

        testInitialLoad(userDataRepo, itemDataSource)
    }

    @Test
    fun loaded_all_ids_on_load_initial_then_continue_loading_by_page() {
        val userDataRepo = UserDataRepo_Both_Success()

        val itemDataSource = ItemDataSource(userDataRepo, disposableCollector)

        testInitialLoad(userDataRepo, itemDataSource)

        var startPosition = initialLoadSize
        var loadRangeCallCount = 1
        while (startPosition < totalIdCount) {
            testLoadByStartPosition(startPosition, loadRangeCallCount, userDataRepo, itemDataSource)
            startPosition += pageSize
            loadRangeCallCount += 1
        }
    }

    @Test
    fun loaded_all_ids_on_load_initial_then_continue_loading_by_page_even_with_random_errors() {
        val userDataRepo = UserDataRepo_LoadIds_Success_LoadDetails_Mixed()

        val itemDataSource = ItemDataSource(userDataRepo, disposableCollector)

        testInitialLoad_withRandomErrors(userDataRepo, itemDataSource)

        var startPosition = initialLoadSize
        var loadRangeCallCount = 1
        while (startPosition < totalIdCount) {
            testLoadByStartPosition_withRandomErrors(startPosition, loadRangeCallCount, userDataRepo, itemDataSource)
            startPosition += pageSize
            loadRangeCallCount += 1
        }
    }


    private fun testInitialLoad(userDataRepo: UserDataRepo_Both_Success, itemDataSource: ItemDataSource<User>) {
        itemDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        argumentCaptor<List<DataWrapper<User>>>().apply {
            verify(loadInitialCallback).onResult(capture(), eq(0) , eq(totalIdCount))

            assertEquals(1, this.allValues.size)

            val userList = ArrayList<User>()
            val dataStateList = ArrayList<DataWrapper.State>()
            this.allValues[0].forEach {
                userList.add(it.data!!)
                dataStateList.add(it.getState())
            }

            assertEquals(userDataRepo.randomUserList.take(initialLoadSize), userList)

            dataStateList.forEach {
                assertEquals(DataWrapper.State.SUCCESS, it)
            }
        }
    }

    private fun testInitialLoad_withRandomErrors(userDataRepo: UserDataRepo_LoadIds_Success_LoadDetails_Mixed, itemDataSource: ItemDataSource<User>) {
        itemDataSource.loadInitial(loadInitialParams, loadInitialCallback)

        argumentCaptor<List<DataWrapper<User>>>().apply {
            //verify if onResult function of callback is called
            verify(loadInitialCallback).onResult(capture(), eq(0) , eq(totalIdCount))
            // check argument captor's number of result returned
            assertEquals(1, this.allValues.size)

            // this list holds our actual result, items can be a User or ErrorMessageIdTuple
            val anyList = ArrayList<Any>()

            // prepare our actual results
            var position = 0
            this.allValues[0].forEach {
                // if success, add User
                if (it.getState() == DataWrapper.State.SUCCESS) {
                    anyList.add(it.data!!)
                }
                // if error add ErrorMessageIdTuple
                else {
                    anyList.add(ErrorMessageIdTuple(userDataRepo.randomIdList[position], it.errorMessage!!))
                }
                position += 1
            }

            // compare actual result to expected result
            var position1 = 0
            userDataRepo.randomAnyList.take(initialLoadSize).forEach {
                val any = anyList[position1]
                if (it is User) {
                    assertEquals(it, any)
                } else if (it is ErrorMessageIdTuple){
                    assertEquals(it.errorMessage, (any as ErrorMessageIdTuple).errorMessage)
                    assertEquals(it.id, any.id)
                }
                position1 += 1
            }
        }
    }

    private fun testLoadByStartPosition(startPosition: Int, loadRangeCallCount: Int,
                                        userDataRepo: UserDataRepo_Both_Success, itemDataSource: ItemDataSource<User>) {

        val loadRangeParams = PositionalDataSource.LoadRangeParams(startPosition, pageSize)
        itemDataSource.loadRange(loadRangeParams, loadRangeCallback)

        argumentCaptor<List<DataWrapper<User>>>().apply {
            verify(loadRangeCallback, times(loadRangeCallCount)).onResult(capture())
            assertEquals(loadRangeCallCount, this.allValues.size)
            assertEquals(pageSize, this.allValues[loadRangeCallCount - 1].size)

            val resultUserList = ArrayList<User>()
            val resultDataStateList = ArrayList<DataWrapper.State>()

            this.allValues[loadRangeCallCount - 1].forEach {
                resultUserList.add(it.data!!)
                resultDataStateList.add(it.getState())
            }

            val expectedUserList = ArrayList<User>()
            for (i in startPosition until (startPosition + pageSize)) {
                expectedUserList.add(userDataRepo.randomUserList[i])
            }

            assertEquals(expectedUserList, resultUserList)

            resultDataStateList.forEach {
                assertEquals(DataWrapper.State.SUCCESS, it)
            }
        }
    }

    private fun testLoadByStartPosition_withRandomErrors(startPosition: Int, loadRangeCallCount: Int,
                                        userDataRepo: UserDataRepo_LoadIds_Success_LoadDetails_Mixed,
                                        itemDataSource: ItemDataSource<User>) {

        val loadRangeParams = PositionalDataSource.LoadRangeParams(startPosition, pageSize)
        itemDataSource.loadRange(loadRangeParams, loadRangeCallback)

        argumentCaptor<List<DataWrapper<User>>>().apply {
            // verify if onResult function of callback is called the 'nth' time using loadRangeCallCount
            verify(loadRangeCallback, times(loadRangeCallCount)).onResult(capture())
            // verify argument captor already captured n values using loadRangeCallCount
            assertEquals(loadRangeCallCount, this.allValues.size)
            assertEquals(pageSize, this.allValues[loadRangeCallCount - 1].size)

            // this list holds our actual result, items can be a User or ErrorMessageIdTuple
            val resultAnyList = ArrayList<Any>()

            // lets start at the start position because load range start at a specific starting int
            var position = startPosition
            this.allValues[loadRangeCallCount - 1].forEach {
                if (it.getState() == DataWrapper.State.SUCCESS) {
                    resultAnyList.add(it.data!!)
                } else {
                    // create ErrorMessageIdTuple using userDataRepo.randomIdList
                    // we need to get the correct id from randomIdList using the correct position
                    // because our total is 100 and our page size is only 5, we iterate to the total by page size
                    resultAnyList.add(ErrorMessageIdTuple(userDataRepo.randomIdList[position], it.errorMessage!!))
                }
                position += 1
            }

            // compare result to expected
            var position1 = 0
            // we use i to get the correct expected value from userDataRepo.randomAnyList as we iterate from
            // start position to start position + page size (ie, 10 to 15, 15 to 20.... n to n+5)
            for (i in startPosition until (startPosition + pageSize)) {

                // result and expected can be a User or ErrorMessageIdTuple
                val resultAny = resultAnyList[position1]
                val expectedAny = userDataRepo.randomAnyList[i]

                if (expectedAny is User) {
                    assertEquals(expectedAny, resultAny)

                } else if (expectedAny is ErrorMessageIdTuple){
                    assertEquals(expectedAny.errorMessage, (resultAny as ErrorMessageIdTuple).errorMessage)
                    assertEquals(expectedAny.id, resultAny.id)
                }
                position1 += 1
            }
        }
    }

}