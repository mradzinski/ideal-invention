package com.mradzinski.probablelamp.common.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Opus RecyclerView scroll listener, that once the RecyclerView reaches the maximum visible items threshold,
 * executes a callback thus allowing the user to load more items.
 *
 * @param layoutManager The [RecyclerView.LayoutManager] currently being used by the RecyclerView.
 * @param visibleItemsThreshold The maximum visible items threshold. Defaults to 5
 * @param initialPage The initial page. Defaults to 0.
 * @param onLoadMore Callback to execute once the RecyclerView reaches the maximum visible items threshold
 */
@Suppress("unused")
class RecyclerViewEndlessScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private var visibleItemsThreshold: Int = 5,
    private val initialPage: Int = 0,
    private var onLoadMore: (currentPage: Int, nextPage: Int, totalItems: Int, view: RecyclerView?) -> Unit
) : RecyclerView.OnScrollListener() {

    // The current offset index of data you have loaded
    private var currentPage = initialPage

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    private var isEnabled: Boolean = true

    /* ********************************************
     *              End of variables              *
     ******************************************** */


    init {
        when (layoutManager) {
            is StaggeredGridLayoutManager -> this.visibleItemsThreshold *= layoutManager.spanCount
            is GridLayoutManager -> this.visibleItemsThreshold *= layoutManager.spanCount
        }
    }


    /* *********************************************
     *             End of constructors             *
     ********************************************* */

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        var lastVisibleItemPosition = 0
        val totalItemCount = layoutManager.itemCount

        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)

                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItemPosition(lastVisibleItemPositions)
            }

            is GridLayoutManager -> {
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }

            is LinearLayoutManager -> {
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = initialPage
            this.previousTotalItemCount = totalItemCount

            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleItemsThreshold >= totalItemCount && isEnabled) {
            recyclerView.post {
                onLoadMore(currentPage, currentPage + 1, totalItemCount, recyclerView)
            }
            loading = true
            currentPage++
        }
    }

    private fun getLastVisibleItemPosition(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }

        return maxSize
    }

    /**
     * Resets the state, for example after a pull to refresh event.
     */
    fun reset() {
        this.currentPage = initialPage
        this.previousTotalItemCount = 0
        this.loading = true
    }

    /**
     * Set the enabled state of this [RecyclerViewEndlessScrollListener].
     */
    fun setEnabled(enabled: Boolean) {
        this.isEnabled = enabled
    }

}