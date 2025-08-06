package dev.oddbyte.isolationist

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.RemoteException
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rosan.dhizuku.api.Dhizuku
import com.rosan.dhizuku.api.DhizukuRequestPermissionListener
import dev.oddbyte.isolationist.ui.theme.IsolationistTheme


/**
 * @author Oddbyte
 * The only advantage I have over vibe coders is that they cant write code this bad
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (!isPackageInstalled("com.rosan.dhizuku", packageManager) || !Dhizuku.isPermissionGranted()) {
            setContent {
                IsolationistTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            TopAppBar(
                                title = {
                                    Text("Isolationist")
                                }
                            )
                            Text(
                                text = "You must install and enable Dhizuku before using this app. Please install and enable Dhizuku, then click the button below.",
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .padding(32.dp, 5.dp)
                            )
                            Button(
                                content = { Text("Check Dhizuku") },
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .padding(32.dp, 5.dp),
                                onClick = {
                                    val i: Intent = Intent(applicationContext, MainActivity::class.java)
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    applicationContext.startActivity(i)
                                }
                            )
                        }
                    }
                }
            }
            if (isPackageInstalled("com.rosan.dhizuku", packageManager)) Dhizuku.requestPermission(object : DhizukuRequestPermissionListener() {
                @Throws(RemoteException::class)
                override fun onRequestPermission(grantResult: Int) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        main()
                    } else {
                        setContent {
                            IsolationistTheme {
                                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                                    Text(
                                        text = "You must allow access to Dhizuku in order to continue.",
                                        modifier = Modifier.padding(innerPadding)
                                    )
                                }
                            }
                        }
                    }
                }
            })
        } else main()
    }

    fun main() {
        setContent {
            IsolationistTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = "Yay is werkings",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        try {
            packageManager.getPackageInfo(packageName, 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }
}

@Composable
fun Text(text: String, modifier: Modifier = Modifier) {
    Text( text, modifier )
}