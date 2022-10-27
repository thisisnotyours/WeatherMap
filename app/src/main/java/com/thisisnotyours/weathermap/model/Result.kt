package com.thisisnotyours.weathermap.model

//enum class = 열거형 클래스.
// 코드가 단순해지며, 가독성 UP
// 인스턴스 생성과 상속을 방지, 상수값의 타입 안전성 보장

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class Result<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Result<T> {
            return Result(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Result<T> {
            return Result(Status.LOADING, data, null)
        }
    }


}