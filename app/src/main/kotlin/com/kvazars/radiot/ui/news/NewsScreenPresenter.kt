package com.kvazars.radiot.ui.news

import com.kvazars.radiot.domain.news.NewsInteractor
import com.kvazars.radiot.ui.shared.NewsItemView
import com.kvazars.radiot.utils.addTo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.threeten.bp.format.DateTimeFormatterBuilder

/**
 * Created by Leo on 27.04.2017.
 */
class NewsScreenPresenter(
        private val view: NewsScreenContract.View,
        newsInteractor: NewsInteractor
) : NewsScreenContract.Presenter {
    //region CONSTANTS -----------------------------------------------------------------------------

    //endregion

    //region CLASS VARIABLES -----------------------------------------------------------------------

    private val disposableBag = CompositeDisposable()
    private val dateFormat = DateTimeFormatterBuilder()
        .appendPattern("dd MMM, HH:mm")
        .toFormatter()

    //endregion

    //region CONSTRUCTOR ---------------------------------------------------------------------------

    init {
        newsInteractor
                .allNews
                .flatMap {
                    Observable.fromIterable(it)
                            .map {
                                NewsItemView.NewsViewModel(
                                        it.title,
                                    "${it.domain} - ${it.time.format(dateFormat)}",
                                        it.snippet,
                                        it.link,
                                        it.pictureUrl
                                )
                            }
                            .toList()
                            .toObservable()
                }
                .subscribe(
                        { view.setNews(it) }
                ).addTo(disposableBag)
    }

    override fun onNewsItemClick(item: NewsItemView.NewsViewModel) {
        view.openNewsUrl(item.link)
    }

    //endregion

    //region LOCAL METHODS -------------------------------------------------------------------------

    fun onDestroy() {
        disposableBag.clear()
    }

    //endregion

    //region INNER CLASSES -------------------------------------------------------------------------

    //endregion
}