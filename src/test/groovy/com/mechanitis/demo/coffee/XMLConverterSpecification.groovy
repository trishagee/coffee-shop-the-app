package com.mechanitis.demo.coffee

import spock.lang.Specification

class XMLConverterSpecification extends Specification {
    String xml = "  <node id=\"20921877\" lat=\"52.2099727\" lon=\"0.1172091\">\n" +
                 "    <tag k=\"amenity\" v=\"cafe\"/>\n" +
                 "    <tag k=\"cuisine\" v=\"coffee_shop\"/>\n" +
                 "  </node>";

    def 'convert'() {
        def xmlSlurper = new XmlSlurper().parseText(xml)

//        def file = new File('src/test/resources/coffee.xml')
//        println file
//        def xmlSlurper = new XmlSlurper().parse(file)

        expect:
        def coffeeShopNode = xmlSlurper;
        println 'size: ' + coffeeShopNode.size()
        Map coffeeShop = ['openStreetMapId': coffeeShopNode.@id,
                          'location'       : ['coordinates': [coffeeShopNode.@lon, coffeeShopNode.@lat],
                                              'type'       : 'Point']]
        coffeeShopNode.tag.each { theNode ->
            String key = theNode.@k.text()
            String value = theNode.@v.text()
            coffeeShop.put(key, value)
        }
        println coffeeShop

    }

    String complex = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                 "<osm version=\"0.6\" generator=\"Overpass API\">\n" +
//                 "<note>The data included in this document is from www.openstreetmap.org. The data is made available under ODbL.</note>\n" +
//                 "<meta osm_base=\"2014-02-17T21:17:01Z\"/>\n" +
                 "\n" +
                 "  <node id=\"20921877\" lat=\"52.2099727\" lon=\"0.1172091\">\n" +
                 "    <tag k=\"amenity\" v=\"cafe\"/>\n" +
                 "    <tag k=\"cuisine\" v=\"coffee_shop\"/>\n" +
                 "  </node>\n" +
                 "  <node id=\"27653001\" lat=\"53.9043868\" lon=\"-1.6937433\">\n" +
                 "    <tag k=\"addr:city\" v=\"Otley\"/>\n" +
                 "    <tag k=\"addr:country\" v=\"GB\"/>\n" +
                 "    <tag k=\"addr:housenumber\" v=\"9\"/>\n" +
                 "    <tag k=\"addr:postcode\" v=\"LS21 3HE\"/>\n" +
                 "    <tag k=\"addr:street\" v=\"Mercury Row\"/>\n" +
                 "    <tag k=\"amenity\" v=\"cafe\"/>\n" +
                 "    <tag k=\"cuisine\" v=\"coffee_shop\"/>\n" +
                 "    <tag k=\"gluten_free\" v=\"yes\"/>\n" +
                 "    <tag k=\"is_in:town\" v=\"Otley\"/>\n" +
                 "    <tag k=\"name\" v=\"The Walkway Cafe\"/>\n" +
                 "  </node>" +
                 "</osm>";

    def 'convert 2'() {
        def xmlSlurper = new XmlSlurper().parseText(complex)

//        def file = new File('src/test/resources/coffee.xml')
//        println file
//        def xmlSlurper = new XmlSlurper().parse(file)

        expect:
        xmlSlurper.node.each { child ->
            println "blah $child, $child.name"
            println "blah " + child.@id
            Map coffeeShop = ['openStreetMapId': child.@id,
                              'location'       : ['coordinates': [child.@lon, child.@lat],
                                                  'type'       : 'Point']]
            child.tag.each { theNode ->
                String key = theNode.@k.text()
                String value = theNode.@v.text()
                coffeeShop.put(key, value)
            }
            println coffeeShop
        }

    }

}