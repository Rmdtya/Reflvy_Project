import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
class AppUsageStatisticsHelper(private val context: Context) {

    fun getScreenOnTime(): Long {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val end = System.currentTimeMillis()
        val begin = end - 24 * 60 * 60 * 1000 // Misalnya, 1 hari terakhir

        val screenOnTime = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, begin, end)

        var totalScreenOnTime = 0L
        for (usageStats in screenOnTime) {
            totalScreenOnTime += usageStats.totalTimeInForeground
        }

        return totalScreenOnTime
    }

    fun getAppUsageTime(packageName: String): Long {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val end = System.currentTimeMillis()
        val begin = end - 24 * 60 * 60 * 1000 // Misalnya, 1 hari terakhir

        val appUsageTime = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, begin, end)

        var totalAppUsageTime = 0L
        for (usageStats in appUsageTime) {
            if (usageStats.packageName == packageName) {
                totalAppUsageTime += usageStats.totalTimeInForeground
            }
        }

        return totalAppUsageTime
    }
}
