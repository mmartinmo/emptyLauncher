package com.qq42labs.screenfreezedemo

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.LauncherApps
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qq42labs.screenfreezedemo.ui.theme.ScreenFreezeDemoTheme

class MainActivity : ComponentActivity() {
    private val deviceWakeAndSleepBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context?, intent: Intent) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            val isInteractive = powerManager.isInteractive

            if (isInteractive) {
                EmptyForegroundService.start(this@MainActivity.applicationContext)
            } else {
                EmptyForegroundService.stop(this@MainActivity.applicationContext)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val launcherAppsService = applicationContext.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps


            ScreenFreezeDemoTheme {
                MyApp(this@MainActivity,launcherAppsService)
            }
        }

        if (!DefaultLauncherChecker(this).isThisAppDefault()) {
            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
            startActivity(intent)
        }

        registerReceiver(deviceWakeAndSleepBroadcastReceiver, IntentFilter(Intent.ACTION_SCREEN_ON))
        registerReceiver(deviceWakeAndSleepBroadcastReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(deviceWakeAndSleepBroadcastReceiver)
        unregisterReceiver(deviceWakeAndSleepBroadcastReceiver)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyApp(context: Context, launcherAppsService: LauncherApps) {
    val pagerState = rememberPagerState(initialPage = 0,initialPageOffsetFraction = 0f,pageCount = { 2})

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HorizontalPager( state = pagerState) { page ->
            when (page) {
                0 -> PageOne(context,launcherAppsService)
                1 -> PageTwo()
            }
        }
    }
}

@Composable
fun PageOne(context: Context, launcherAppsService: LauncherApps) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Using a loop to display "Item N" five times
            Text("Calendar", Modifier.align(Alignment.Center).padding(top = (130).dp).clickable { startCalendarApp(context,launcherAppsService) })
            Text("WhatsApp", Modifier.align(Alignment.Center).padding(top = (260).dp).clickable { startWhatsAppApp(context,launcherAppsService) })
            Text("Firefox", Modifier.align(Alignment.Center).padding(top = (390).dp).clickable { startFirefoxApp(context,launcherAppsService) })
            Text("Opera Touch", Modifier.align(Alignment.Center).padding(top = (520).dp).clickable { startOperaTouchApp(context,launcherAppsService) })
            Text("Chrome", Modifier.align(Alignment.Center).padding(top = (650).dp).clickable { startChromeApp(context,launcherAppsService) })
    }
}

fun startCalendarApp(context: Context, launcherAppsService: LauncherApps) {
    val component = ComponentName("com.google.android.calendar", "com.android.calendar.AllInOneActivity")
    launchComponent(context, launcherAppsService, component)
}

fun startWhatsAppApp(context: Context, launcherAppsService: LauncherApps) {
    val component = ComponentName("com.whatsapp", "com.whatsapp.Main")
    launchComponent(context, launcherAppsService, component)
}

fun startFirefoxApp(context: Context, launcherAppsService: LauncherApps) {
    val component = ComponentName("org.mozilla.firefox", "org.mozilla.firefox.App")
    launchComponent(context, launcherAppsService, component)
}

fun startOperaTouchApp(context: Context, launcherAppsService: LauncherApps) {
    val component = ComponentName("com.opera.touch", "com.opera.touch.MainActivity")
    launchComponent(context, launcherAppsService, component)
}

fun startChromeApp(context: Context, launcherAppsService: LauncherApps) {
    val component = ComponentName("com.android.chrome", "com.google.android.apps.chrome.Main")
    launchComponent(context, launcherAppsService, component)
}

private fun launchComponent(context: Context,launcherAppsService: LauncherApps, component: ComponentName) {
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val packageManager = context.packageManager
    val foundApps: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
    val app = foundApps.find { it.activityInfo.packageName == component.packageName }

    app?.let {
        val newComponent = ComponentName(app.activityInfo.packageName, app.activityInfo.name)
        launcherAppsService.startMainActivity(newComponent, Process.myUserHandle(), null, null)
    }
}

@Composable
fun PageTwo() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text("X", style = MaterialTheme.typography.displayLarge)
    }
}

