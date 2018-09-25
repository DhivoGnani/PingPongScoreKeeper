package me.dhivo.android.pingpongmatchtracker.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView


import me.dhivo.android.pingpongmatchtracker.R
import me.dhivo.android.pingpongmatchtracker.components.PingPongSet

class SetAdapter(context: Context, resource: Int, private val sets: List<PingPongSet>) : ArrayAdapter<PingPongSet>(context, resource, sets) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val set = getItem(position)

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_editable_set_details, parent, false)
        }

        val playerOneScore: EditText = convertView!!.findViewById(R.id.player_one_num)
        val playerTwoScore: EditText = convertView.findViewById(R.id.player_two_num)
        val title = convertView.findViewById<TextView>(R.id.set_n)

        title.text = "SET " + set!!.setNumber

        if (set.playerOneScore >= 0) {
            playerOneScore.setText(set.playerOneScore.toString())
        } else {
            playerOneScore.setText("")
        }

        if (set.playerTwoScore >= 0) {
            playerTwoScore.setText(set.playerTwoScore.toString())
        } else {
            playerTwoScore.setText("")
        }

        val currentSet = sets[position]

        playerOneScore.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                try {

                    currentSet.playerOneScore = Integer.valueOf(s.toString())
                } catch (ex: Exception) {

                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
            }
        })

        playerTwoScore.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                try {

                    currentSet.playerTwoScore = Integer.valueOf(s.toString())
                } catch (ex: Exception) {
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
            }
        })

        return convertView
    }
}
