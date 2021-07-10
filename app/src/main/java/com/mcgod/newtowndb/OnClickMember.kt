package com.mcgod.newtowndb

import androidx.recyclerview.widget.RecyclerView

interface OnClickMember {

   fun  click(holder: RecyclerView.ViewHolder , member : Members)
}