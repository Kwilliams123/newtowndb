package com.mcgod.newtowndb


    fun removeDuplicates(s: String, k: Int): String {
        
        var copy = s
        var  b = 1
        var c = 1
        var i = 0
        var hold = 0

        var l = s.length
        while( i < l-k  ){

                while (b < k){

                    if(copy[i].equals(copy[b+i]) ){
                         hold = if (i < hold)  i else  hold

                        c++
                    }else{

                        break
                    }

                    b++

                }

                //  println(c)
                if(c == k){

                   copy =  copy.removeRange(hold, hold+b)

                    removeDuplicates(copy, k)

                }


            i++

            l = copy.length

        }


        return copy
        
    }



    fun main(){

       println (removeDuplicates("pbbcggttciiippooaais", 2))

       //println( "Akwasi".removeRange(0, 3) )
        //println("Remove".removeRange(0,1))
    }
