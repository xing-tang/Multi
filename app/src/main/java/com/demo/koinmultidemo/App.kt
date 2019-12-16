package com.demo.koinmultidemo
import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.demo.base.di.baseModule
import com.demo.koinmultidemo.di.appModule
import com.demo.order.di.orderModule
import com.demo.user.di.userModule
import com.jeremyliao.liveeventbus.LiveEventBus
import me.yokeyword.fragmentation.Fragmentation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        private var instance: App by Delegates.notNull()
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ARouter.init(this)
        initFragmentation()
        initInjector()
        initBus()
    }

    private fun initBus() {
        LiveEventBus
            .config()
            .supportBroadcast(this)
            .lifecycleObserverAlwaysActive(true)
    }

    /**
     *  Koin 注入不同的模块的网络请求
     */
    private fun initInjector() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(
                appModule,
                baseModule,
                userModule,
                orderModule
            ))
        }
    }

    private fun initFragmentation() {
        if (BuildConfig.DEBUG) {
            Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true) // 实际场景建议.debug(BuildConfig.DEBUG)
                /**
                 * 可以获取到[me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning]
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException {

                }
                .install()
        }
    }


}