package my.portfolio.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.btn_start).setOnClickListener {
            startActivity(Intent(this,GameActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.btn_scores).setOnClickListener {
            startActivity(Intent(this,ScoresActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this,SettingsActivity::class.java))
        }
        findViewById<MaterialButton>(R.id.btn_exit).setOnClickListener {
            this.finish()
        }
    }

}