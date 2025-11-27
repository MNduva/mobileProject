package com.example.semesterproject.data

object MachineImageUrls {
    val TRACTOR = "https://agfundernews.com/wp-content/uploads/2022/08/281832544_4872557739523316_4916680536175142455_n.jpeg"
    val SPRAYER = "https://www.myagrovet.co.ke/images/products/7512/0300a59ebd25c4e783bc74fb4f13a4fd.jpg"
    val CULTIVATOR = "https://cdn.britannica.com/84/73384-050-6DFF3413/crop-cultivator.jpg"
    val BALER = "https://www.anis-trend.com/wp-content/uploads/2021/03/anis-imagesL-c6.jpg"
    val HARVESTER = "https://www.deere.com/assets/images/region-4/products/harvesting/r4k010997-rrd-1365x768.jpg"
    val IRRIGATOR = "https://www.rdsmme.com/wp-content/uploads/2022/03/AgricturalSprayer-2048x1363.jpg"
    val HARROW = "https://m.media-amazon.com/images/I/91qN5jUo0lL._AC_UF1000,1000_QL80_.jpg"
    
    fun getAllImages(): List<String> {
        return listOf(
            TRACTOR,
            SPRAYER,
            CULTIVATOR,
            BALER,
            HARVESTER,
            IRRIGATOR,
            HARROW
        ).filter { it.isNotBlank() }
    }
}
