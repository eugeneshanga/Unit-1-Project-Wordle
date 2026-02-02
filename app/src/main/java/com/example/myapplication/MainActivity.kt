package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etGuess: EditText
    private lateinit var btnSubmit: Button

    private lateinit var tvGuess1: TextView
    private lateinit var tvResult1: TextView
    private lateinit var tvGuess2: TextView
    private lateinit var tvResult2: TextView
    private lateinit var tvGuess3: TextView
    private lateinit var tvResult3: TextView
    private lateinit var tvAnswer: TextView

    private var guessCount = 0
    private var wordToGuess = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Grab views
        etGuess = findViewById(R.id.etGuess)
        btnSubmit = findViewById(R.id.btnSubmit)

        tvGuess1 = findViewById(R.id.tvGuess1)
        tvResult1 = findViewById(R.id.tvResult1)
        tvGuess2 = findViewById(R.id.tvGuess2)
        tvResult2 = findViewById(R.id.tvResult2)
        tvGuess3 = findViewById(R.id.tvGuess3)
        tvResult3 = findViewById(R.id.tvResult3)

        tvAnswer = findViewById(R.id.tvAnswer)

        // Start a new game
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()
        tvAnswer.text = "" // hidden at start

        btnSubmit.setOnClickListener {
            if (btnSubmit.text.toString() == "RESTART") {
                resetGame()
            } else {
                handleSubmit()
            }
        }
    }

    private fun handleSubmit() {
        if (guessCount >= 3) {
            Toast.makeText(this, "No guesses left!", Toast.LENGTH_SHORT).show()
            return
        }

        val rawGuess = etGuess.text.toString().trim()
        if (rawGuess.length != 4) {
            Toast.makeText(this, "Enter a 4-letter word", Toast.LENGTH_SHORT).show()
            return
        }
        if (!rawGuess.all { it.isLetter() }) {
            Toast.makeText(this, "Use letters A–Z only", Toast.LENGTH_SHORT).show()
            return
        }
        val guess = rawGuess.uppercase()
        val result = checkGuess(guess)
        val colored = coloredGuess(guess)

        when (guessCount) {
            0 -> {
                tvGuess1.text = guess
                tvResult1.text = colored
            }
            1 -> {
                tvGuess2.text = guess
                tvResult2.text = colored
            }
            2 -> {
                tvGuess3.text = guess
                tvResult3.text = colored
            }
        }

        guessCount++
        etGuess.text.clear()

        // End of game: disable submit + reveal answer
        if (guessCount >= 3) {
            btnSubmit.isEnabled = true
            btnSubmit.text = "RESTART"
            tvAnswer.text = "Answer: $wordToGuess"
        }
    }
    // author: calren
    object FourLetterWordList {
        // List of most common 4 letter words from: https://7esl.com/4-letter-words/
        val fourLetterWords =
            "Area,Army,Baby,Back,Ball,Band,Bank,Base,Bill,Body,Book,Call,Card,Care,Case,Cash,City,Club,Cost,Date,Deal,Door,Duty,East,Edge,Face,Fact,Farm,Fear,File,Film,Fire,Firm,Fish,Food,Foot,Form,Fund,Game,Girl,Goal,Gold,Hair,Half,Hall,Hand,Head,Help,Hill,Home,Hope,Hour,Idea,Jack,John,Kind,King,Lack,Lady,Land,Life,Line,List,Look,Lord,Loss,Love,Mark,Mary,Mind,Miss,Move,Name,Need,News,Note,Page,Pain,Pair,Park,Part,Past,Path,Paul,Plan,Play,Post,Race,Rain,Rate,Rest,Rise,Risk,Road,Rock,Role,Room,Rule,Sale,Seat,Shop,Show,Side,Sign,Site,Size,Skin,Sort,Star,Step,Task,Team,Term,Test,Text,Time,Tour,Town,Tree,Turn,Type,Unit,User,View,Wall,Week,West,Wife,Will,Wind,Wine,Wood,Word,Work,Year,Bear,Beat,Blow,Burn,Call,Care,Cast,Come,Cook,Cope,Cost,Dare,Deal,Deny,Draw,Drop,Earn,Face,Fail,Fall,Fear,Feel,Fill,Find,Form,Gain,Give,Grow,Hang,Hate,Have,Head,Hear,Help,Hide,Hold,Hope,Hurt,Join,Jump,Keep,Kill,Know,Land,Last,Lead,Lend,Lift,Like,Link,Live,Look,Lose,Love,Make,Mark,Meet,Mind,Miss,Move,Must,Name,Need,Note,Open,Pass,Pick,Plan,Play,Pray,Pull,Push,Read,Rely,Rest,Ride,Ring,Rise,Risk,Roll,Rule,Save,Seek,Seem,Sell,Send,Shed,Show,Shut,Sign,Sing,Slip,Sort,Stay,Step,Stop,Suit,Take,Talk,Tell,Tend,Test,Turn,Vary,View,Vote,Wait,Wake,Walk,Want,Warn,Wash,Wear,Will,Wish,Work,Able,Back,Bare,Bass,Blue,Bold,Busy,Calm,Cold,Cool,Damp,Dark,Dead,Deaf,Dear,Deep,Dual,Dull,Dumb,Easy,Evil,Fair,Fast,Fine,Firm,Flat,Fond,Foul,Free,Full,Glad,Good,Grey,Grim,Half,Hard,Head,High,Holy,Huge,Just,Keen,Kind,Last,Late,Lazy,Like,Live,Lone,Long,Loud,Main,Male,Mass,Mean,Mere,Mild,Nazi,Near,Neat,Next,Nice,Okay,Only,Open,Oral,Pale,Past,Pink,Poor,Pure,Rare,Real,Rear,Rich,Rude,Safe,Same,Sick,Slim,Slow,Soft,Sole,Sore,Sure,Tall,Then,Thin,Tidy,Tiny,Tory,Ugly,Vain,Vast,Very,Vice,Warm,Wary,Weak,Wide,Wild,Wise,Zero,Ably,Afar,Anew,Away,Back,Dead,Deep,Down,Duly,Easy,Else,Even,Ever,Fair,Fast,Flat,Full,Good,Half,Hard,Here,High,Home,Idly,Just,Late,Like,Live,Long,Loud,Much,Near,Nice,Okay,Once,Only,Over,Part,Past,Real,Slow,Solo,Soon,Sure,That,Then,This,Thus,Very,When,Wide"

        // Returns a list of four letter words as a list
        fun getAllFourLetterWords(): List<String> {
            return fourLetterWords.split(",")
        }

        // Returns a random four letter word from the list in all caps
        fun getRandomFourLetterWord(): String {
            val allWords = getAllFourLetterWords()
            val randomNumber = (0..allWords.size).shuffled().last()
            return allWords[randomNumber].uppercase()
        }
    }

    /**
     * Returns a String of 'O', '+', and 'X', where:
     * 'O' = right letter, right place
     * '+' = right letter, wrong place
     * 'X' = letter not in the word
     */
    private fun checkGuess(guess: String): String {
        var result = ""
        for (i in 0..3) {
            result += when {
                guess[i] == wordToGuess[i] -> "O"
                guess[i] in wordToGuess -> "+"
                else -> "X"
            }
        }
        return result
    }
    private fun coloredGuess(guess: String): SpannableString {
        val spannable = SpannableString(guess)

        // Status per position: 0 = not in word, 1 = in word wrong spot, 2 = correct spot
        val status = IntArray(4) { 0 }

        // Count letters in target
        val counts = mutableMapOf<Char, Int>()
        for (c in wordToGuess) counts[c] = (counts[c] ?: 0) + 1

        // Pass 1: exact matches (green)
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                status[i] = 2
                counts[guess[i]] = counts[guess[i]]!! - 1
            }
        }

        // Pass 2: wrong position but exists (yellow)
        for (i in 0..3) {
            if (status[i] == 0) {
                val c = guess[i]
                val remaining = counts[c] ?: 0
                if (remaining > 0) {
                    status[i] = 1
                    counts[c] = remaining - 1
                }
            }
        }

        // Apply colors
        val green = ContextCompat.getColor(this, android.R.color.holo_green_dark)
        val yellow = ContextCompat.getColor(this, android.R.color.holo_orange_light)
        val red = ContextCompat.getColor(this, android.R.color.holo_red_dark)

        for (i in 0..3) {
            val color = when (status[i]) {
                2 -> green
                1 -> yellow
                else -> red
            }
            spannable.setSpan(
                ForegroundColorSpan(color),
                i,
                i + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }

    private fun resetGame() {
        guessCount = 0
        wordToGuess = FourLetterWordList.getRandomFourLetterWord()

        tvGuess1.text = ""
        tvResult1.text = ""
        tvGuess2.text = ""
        tvResult2.text = ""
        tvGuess3.text = ""
        tvResult3.text = ""

        tvAnswer.text = ""
        etGuess.text.clear()

        btnSubmit.isEnabled = true
        btnSubmit.text = "GUESS!"
    }
}
