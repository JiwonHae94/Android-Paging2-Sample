package com.jiwon.android_paging2_sample.repositories

import android.app.appsearch.GetByDocumentIdRequest
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jiwon.android_paging2_sample.api.GithubService
import com.jiwon.android_paging2_sample.api.IN_QUALIFIER
import com.jiwon.android_paging2_sample.data.Repo
import retrofit2.HttpException
import java.io.IOException

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
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER

        return try{
            val response = service.searchRepos(apiQuery, position, params.loadSize)
            val repos = response.items
            val nextKey = if(repos.isEmpty()){
                null
            } else{
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if(position == GITHUB_STARTING_PAGE_INDEX) null else position -1,
                nextKey = nextKey
            )
        }catch(e : IOException){
            return LoadResult.Error(e)
        }catch(e : HttpException){
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

}