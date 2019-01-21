package com.kurt.lemond.hackernews.activity_main.repository.model

import com.google.gson.annotations.SerializedName

/**
 * Used to wrap the data returned by the server.
 * Should be used on every retrofit interface class' method's return definitions.
 * In order to wrap data using this class, you should manually change the body of the response on your interceptors.
 */
class DataWrapper<T> {

    enum class State(private val stateName: String) {
        NO_NETWORK("no_network"),
        ERROR("error"),
        SUCCESS("success");

        override fun toString(): String {
            return stateName
        }}

    @SerializedName("data")
    var data: T? = null

    var errorMessage: String? = null

    @SerializedName("state")
    var stateString: String = State.ERROR.toString()

    fun getState(): State {
        return when (stateString) {
            State.NO_NETWORK.toString() -> State.NO_NETWORK
            State.SUCCESS.toString() -> State.SUCCESS
            else -> {
                State.ERROR
            }
        }
    }

    fun setState(state: State) {
        stateString = state.toString()
    }
}