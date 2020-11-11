package my.portfolio.tictactoe
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class GameActivity : AppCompatActivity(){

    private var turnX = true
    private val TYPE_NOLIK = 0
    private val TYPE_KRESTIK = 1
    private var step = 0
    private var scoreX = 0
    private var scoreO = 0

    private val sharedPref: SharedPreferences? by lazy { this.getPreferences(Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val tv = findViewById<MaterialTextView>(R.id.winnerName)
        val tvScoreX = findViewById<MaterialTextView>(R.id.score_x).apply {
            text = getString(R.string.score_x, sharedPref?.getInt("scoreX",0))
        }
        val tvScoreO = findViewById<MaterialTextView>(R.id.score_o).apply {
            text = getString(R.string.score_o, sharedPref?.getInt("scoreO",0))
        }

        val elements: Array<Array<Element>> =
            arrayOf(
                    arrayOf(
                            Element(true, findViewById(R.id.btn_00)),
                            Element(true, findViewById(R.id.btn_01)),
                            Element(true, findViewById(R.id.btn_02))
                    ),
                    arrayOf(
                            Element(true, findViewById(R.id.btn_10)),
                            Element(true, findViewById(R.id.btn_11)),
                            Element(true, findViewById(R.id.btn_12))
                    ),
                    arrayOf(
                            Element(true, findViewById(R.id.btn_20)),
                            Element(true, findViewById(R.id.btn_21)),
                            Element(true, findViewById(R.id.btn_22))
                    )
            )

        setClickListeners(elements, tv, tvScoreX, tvScoreO)
        findViewById<MaterialButton>(R.id.btn_reset).setOnClickListener {
            elements.reset()
        }

    }

    private fun onClick(element: Element){
        element.isEmpty = false
        if (turnX) {
            element.button.setBackgroundResource(R.drawable.ic_x)
            element.type = TYPE_KRESTIK
        }
        else{
            element.button.setBackgroundResource(R.drawable.ic_o)
            element.type = TYPE_NOLIK
        }
        turnX = !turnX
        step++
    }

    private fun setClickListeners(elements: Array<Array<Element>>, tv: MaterialTextView, tvScoreX: MaterialTextView, tvScoreO: MaterialTextView){
        elements.forEachIndexed { indexRow, arrayOfElements ->
            arrayOfElements.forEachIndexed { indexColumn, element ->
                element.button.setOnClickListener {
                    if (element.isEmpty){
                        onClick(element)
                        val hasWinner = checkWinner(elements, indexRow, indexColumn)
                        if (step == 9 && !hasWinner){
                            tv.text = getString(R.string.no_winner)
                            endGame(elements)
                        }
                        else if (hasWinner){
                            endGame(elements)
                            val winner =
                                if (turnX){
                                    tvScoreO.text = getString(R.string.score_o, ++scoreO)
                                    "O"
                                } else{
                                    tvScoreX.text = getString(R.string.score_x, ++scoreX)
                                    "X"
                                }
                            tv.text = getString(R.string.winner_is, winner)
                        }
                    }
                }
            }
        }
    }

    private fun Array<Array<Element>>.reset(){
        this.forEach {
            it.forEach { element ->
                element.apply {
                    isEmpty = true
                    button.setBackgroundColor(
                            ContextCompat.getColor(this@GameActivity, R.color.design_default_color_primary))
                    type = -1
                    button.isEnabled = true
                }
            }
        }
        step = 0
        findViewById<MaterialTextView>(R.id.winnerName).text = ""
    }

    private fun endGame(elements: Array<Array<Element>>){
        elements.forEach {
            it.forEach { element ->
                element.button.isEnabled = false
            }
        }
    }

    private fun checkWinner(elements: Array<Array<Element>>, row: Int, column: Int) : Boolean{
        val current = elements[row][column]
        //Horizontal check
        var hasWin = true
        for (i in elements[row]){
            if(i.type != current.type){
                hasWin = false
                break
            }
        }
        if (hasWin) return true
        //Vertical check
        hasWin = true
        for (i in elements)
            if (i[column].type != current.type){
                hasWin = false
                break
            }
        if (hasWin) return true
        //Diagonal 1
        hasWin = true
        for (i in 0..2){
            if (elements[i][i].type != current.type){
                hasWin = false
                break
            }
        }
        if (hasWin) return true
        //Diagonal 2
        return (elements[0][2].type == current.type && elements[2][0].type == current.type && elements[1][1].type == current.type)

    }

    override fun onDestroy() {
        sharedPref?.edit()?.apply{
            putInt("scoreX",scoreX)
            putInt("scoreO",scoreO)
            apply()
        }
        super.onDestroy()
    }

}
