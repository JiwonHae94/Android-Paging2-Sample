package com.jiwon.android_paging2_sample.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jiwon.android_paging2_sample.api.GithubService
import com.jiwon.android_paging2_sample.data.Repo

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource(
    private val service : GithubService,
    private val query : String
) : PagingSource<Int, Repo>() {

    /**
     * @param params : include key of the page to be loaded and the load size
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        TODO("Not yet implemented")
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        TODO("Not yet implemented")
    }

}