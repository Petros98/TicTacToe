package my.portfolio.tictactoe

import com.google.android.material.button.MaterialButton

data class Element(
    var isEmpty: Boolean = true,
    var button: MaterialButton,
    var type: Int = -1
)