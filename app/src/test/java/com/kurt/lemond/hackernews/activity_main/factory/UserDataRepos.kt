package com.kurt.lemond.hackernews.activity_main.factory

import com.kurt.lemond.hackernews.activity_main.repository.DataRepository
import com.kurt.lemond.hackernews.exception.NoInternetException
import io.reactivex.Single
import kotlin.RuntimeException

const val totalIdCount = 100

private fun searchUserById(userList: List<User>, id: Long): User {
    for (user in userList) {
        if (user.id == id) {
            return user
        }
    }
    throw IllegalArgumentException("Cannot find user due to wrong id!")
}

private fun searchUserOrTupleById(anyList: List<Any>, id: Long): Any {
    for (any in anyList) {
        if (any is User && any.id == id) {
            return any
        } else if (any is ErrorMessageIdTuple && any.id == id) {
            return any
        }
    }
    throw IllegalArgumentException("Cannot find anything due to wrong id!")
}

class ErrorMessageIdTuple(val id: Long, val errorMessage: String)


class UserDataRepo_Both_Success: DataRepository<User>() {

    val randomIdList = createRandomLongList(totalIdCount)

    val randomUserList = ArrayList<User>()

    init {
        for (i in 0 until totalIdCount) {
            randomUserList.add(createRandomUser(randomIdList[i]))
        }
    }

    override fun loadIds(): Single<List<Long>> {
        return Single.just(randomIdList)
    }

    override fun loadDetails(storyId: Long): Single<User> {
        return Single.just(searchUserById(randomUserList, storyId))
    }

}

class UserDataRepo_LoadIds_Success_LoadDetails_Mixed: DataRepository<User>() {

    val randomIdList = createRandomLongList(totalIdCount)

    val randomAnyList = ArrayList<Any>()

    init {
        for (i in 0 until totalIdCount) {
            if (createRandomBoolean()) {
                randomAnyList.add(createRandomUser(randomIdList[i]))
            } else {
                randomAnyList.add(ErrorMessageIdTuple(randomIdList[i], createRandomString()))
            }
        }
    }

    override fun loadIds(): Single<List<Long>> {
        return Single.just(randomIdList)
    }

    override fun loadDetails(storyId: Long): Single<User> {

        val any = searchUserOrTupleById(randomAnyList, storyId)
        if (any is User) {
            return Single.just(any)

        } else if (any is ErrorMessageIdTuple) {
            return Single.error(RuntimeException(any.errorMessage))
        }

        throw RuntimeException("This is not expected to be thrown")
    }
}

class UserDataRepos_Both_NoNetwork: DataRepository<User>() {

    override fun loadIds(): Single<List<Long>> {
        return Single.error(NoInternetException())
    }

    override fun loadDetails(storyId: Long): Single<User> {
        return Single.error(NoInternetException())
    }

}

class UserDataRepos_Both_Failed: DataRepository<User>() {

    val errorMessageLoadId = createRandomString()

    override fun loadIds(): Single<List<Long>> {
        return Single.error(RuntimeException(errorMessageLoadId))
    }

    override fun loadDetails(storyId: Long): Single<User> {
        return Single.error(RuntimeException(errorMessageLoadId))
    }

}