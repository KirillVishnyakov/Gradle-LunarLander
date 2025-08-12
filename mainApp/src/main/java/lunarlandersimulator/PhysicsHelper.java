/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lunarlandersimulator;

import java.util.HashMap;

/**
 * This class contains the derived math and physics formula/constants.
 * @author Kirill Vishnyakov
 */
public class PhysicsHelper {
    private static final double GRAVITATIONAL_CONSTANT_EARTH = 9.8; // 9.8(m/s^2), this is the acceleration due to the gravitation at the surface of the earth for any mass m.
    private static final double UNIVERSAL_GAVITATIONAL_CONSTANT = 6.6743 / 1e11; // 6.6743 * 10^-11 [m3/(kg*s^2)]
    private static HashMap<String, Double> planetRadii = new HashMap<>();
    public PhysicsHelper(){

    }
    /**
     * this method maps different planets to their respective radius in meters.
     */
    public static void fillThePlanetMap(){
        planetRadii.put("Sun", 696340000.0);
        planetRadii.put("Mercury", 2439700.0);
        planetRadii.put("Venus", 6051800.0);
        planetRadii.put("Earth", 6371000.0);
        planetRadii.put("Moon", 1737100.0);
        planetRadii.put("Mars", 3389500.0);
        planetRadii.put("Jupiter", 69911000.0);
        planetRadii.put("Saturn", 58232000.0);
        planetRadii.put("Uranus", 25362000.0);
        planetRadii.put("Neptune", 24622000.0);
        planetRadii.put("Ceres", 473000.0);
        planetRadii.put("Phobos", 11200.0);
        planetRadii.put("Io", 3643200.0);
        planetRadii.put("Europa", 3121000.0);
        planetRadii.put("Ganymede", 5268000.0);
        planetRadii.put("Callisto", 4821000.0);
        planetRadii.put("Deimos", 6200.0);
        planetRadii.put("Mimas", 198200.0);
        planetRadii.put("Enceladus", 252100.0);
        planetRadii.put("Tethys", 531100.0);
        planetRadii.put("Dione", 561400.0);
        planetRadii.put("Rhea", 763800.0);
        planetRadii.put("Titan", 2575500.0);
        planetRadii.put("Iapetus", 1465600.0);
        planetRadii.put("Pallas", 260000.0);
        planetRadii.put("Miranda", 235800.0);
        planetRadii.put("Ariel", 578900.0);
        planetRadii.put("Umbriel", 584700.0);
        planetRadii.put("Titania", 788900.0);
        planetRadii.put("Oberon", 761400.0);
        planetRadii.put("Vesta", 262500.0);
        planetRadii.put("Proteus", 21000.0);
        planetRadii.put("Triton", 1353400.0);
        planetRadii.put("Pluto", 1188300.0);
        planetRadii.put("Charon", 606000.0);
        planetRadii.put("Eris", 1163000.0);
        planetRadii.put("Haumea", 816000.0);
        planetRadii.put("67P-CG", 1163000.0);
        planetRadii.put("Amon Gus", 1163000.0);
    
    }
    /**
     * Gets the value from the key in the parameters.
     * @param planetName name of the planet.
     * @return the radius of the planet
     */
    public static double radiusOfPlanet(String planetName){
        return planetRadii.get(planetName.replace("Set Planet : ", "").replace("Current Planet : ", ""));
    }
    /**
     * This formula is derived from the gravitational Field formula that is used to find the 
     * gravitational force between two masses.
     * @param Planet
     * @param shipHeight
     * @return acceleration due to gravitational force before the correction.
     */
    public static double gravitationalFieldBeforeCorrection(String Planet, double shipHeight) {
        
        double planetRadius = radiusOfPlanet(Planet);
        //System.out.println(planetRadius +" / "+ planetRadius + " + " + shipHeight);
        return GRAVITATIONAL_CONSTANT_EARTH * (planetRadius/(planetRadius + shipHeight)) * (planetRadius/(planetRadius + shipHeight));
    }
        /*
            F = mg = (GMm / R^2) this is at the surface of the planet
            F' = (GMm / (R+x)^2) this is at a height x above the surface of the planet.
                                 R is the planets radius
            so, 
                F'/F = (R / (R + x))^2
                F' = F * (R / (R + x))^2
                m(g') = m(g) * (R / (R + x))^2
        
                g' = g * (R / (R + x))^2
        
                the goal is to find the acceleration due to the gravitational force of planet P at x distnace from the surface.
                
                gP(x) = gP(0) *  (RP / (RP + x))^2
        
                We also know that the acceleration due to Gforce of any planet can be written in terms of 
                the acceleration due to Gforce of the earth ->
                
                gp(0) = C * 9.8   (ex: gMoon(0) = Cmoon * 9.8 =  0.16 * 9.8 = 1.57 N)
        
                so,
                gP(x) = C * [9.8 *  (RP / (RP + x))^2]
        
        
                gP(x)/C = 9.8 *  (RP / (RP + x))^2
        
                The above forumla is exaclty what is being returned by that method.
        */
}
