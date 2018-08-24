package milu.kiriu2010.loader

data class AsyncResult<T>(
        var exception: Exception? = null,
        var data: T? = null
)
