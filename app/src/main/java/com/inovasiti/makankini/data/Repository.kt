package com.inovasiti.makankini.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
//ActivityRetainedScoped = should exist for the life of an activity, surviving configuration
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource) {

    val remote = remoteDataSource
}