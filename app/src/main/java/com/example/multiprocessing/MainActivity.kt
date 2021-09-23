package com.example.multiprocessing

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.multiprocessing.workers.NotificationWorker


class MainActivity : AppCompatActivity() {


    companion object {
        const val MESSAGE_STATUS = "message_status"
        const val WORK_RESULT = "work_result"

    }

    lateinit var mWorkManager: WorkManager
    lateinit var mRequest: OneTimeWorkRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textView = findViewById<TextView>(R.id.textView)
        mWorkManager = WorkManager.getInstance()

        var inputData = Data.Builder()
            .putString(MESSAGE_STATUS, "My value")
            .build()

        mRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(inputData).build()



        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(
            this,
            androidx.lifecycle.Observer {
                if (it != null) {
                    val state = it.state
                    val outputdata=it.outputData

                    textView.append(""" $state """.trimIndent())
                    textView.append(""" ${outputdata.getString(WORK_RESULT)} """.trimIndent())

                }

            })


    }


    public fun click(view: View) {


        mWorkManager.enqueue(mRequest);


    }
}