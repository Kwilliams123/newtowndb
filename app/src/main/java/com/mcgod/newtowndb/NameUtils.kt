package com.mcgod.newtowndb

class NameUtils(val name :String) {

    fun getAlphaPic() = mapOfDrawable[name.first().toLowerCase()]

    companion object{

        val mapOfDrawable = mapOf(

        'a' to  R.drawable.ic_a,
        'b' to  R.drawable.ic_b,
        'c' to  R.drawable.ic_c,
        'd' to  R.drawable.ic_d,
        'e' to  R.drawable.ic_e,
        'f' to  R.drawable.ic_f,
        'g' to  R.drawable.ic_g,
        'h' to  R.drawable.ic_h,
        'i' to  R.drawable.ic_i,
        'j' to  R.drawable.ic_j,
        'k' to  R.drawable.ic_k,
        'l' to  R.drawable.ic_l,
        'm' to  R.drawable.ic_m,
        'n' to  R.drawable.ic_n,
        'o' to  R.drawable.ic_o,
        'p' to  R.drawable.ic_p,
        'q' to  R.drawable.ic_q,
        'r' to  R.drawable.ic_r,
        's' to  R.drawable.ic_s,
        't' to  R.drawable.ic_t,
        'u' to  R.drawable.ic_u,
        'v' to  R.drawable.ic_v,
        'w' to  R.drawable.ic_w,
        'x' to  R.drawable.ic_x,
        'y' to  R.drawable.ic_y,
        'z' to  R.drawable.ic_z
        )
    }


}