/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lab8_povilas_sidaravicius;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import studijosKTU.DataKTU;
import studijosKTU.Ks;

/**
 *
 * @author PovilasSid
 */
public class Place implements DataKTU, Comparable<Place> {

    private String name;
    private double latetude;
    private double longetude;
    private int sparks;
    private String type;
    static private long seed = System.currentTimeMillis();
    private final String id;
    
    static private int  MIN_CORDINATE_ACCURACY = 5;
    static private double CURRENT_LATETUDE = 54.920619;
    static private double CURRENT_LONGETUDE = 23.964699;

    public Place() {
        this.id = String.valueOf(seed++);
    }

    public Place(String name, double latetude, double longetude, int stars, String type) {
        this.name = name;
        this.latetude = latetude;
        this.longetude = longetude;
        this.sparks = stars;
        this.type = type;
        this.id = String.valueOf(seed++);
        
    }

    public Place(String e) {
        this.id = String.valueOf(seed++);
        this.fromString(e);
    }

    @Override
    public Place create(String dataString) {
        return new Place(dataString);
    }

    @Override
    public String validate() {
        String errorType = "";
        
        String[] latetudeSplit = Double.toString(latetude).split("\\.");
        String[] longetudeSplit = Double.toString(longetude).split("\\.");
        if (longetudeSplit[1].length() < MIN_CORDINATE_ACCURACY && latetudeSplit[1].length() < MIN_CORDINATE_ACCURACY){
            errorType = "Permazas platumos ir koordinatciu tikslumas";
        }else if (latetudeSplit[1].length() < MIN_CORDINATE_ACCURACY) {
            errorType = "Permazas platumos koordinates tikslumas";
        }else if (longetudeSplit[1].length() < MIN_CORDINATE_ACCURACY) {
            errorType = "Permazas ilgumos koordinates tikslumas";
        }
        
        if(latetude<PlaceGenerator.MIN_LAT && latetude> PlaceGenerator.MAX_LAT){
            errorType = "Ivesta negalima platumos koordinates reike. Mazesne, nei "
                    + PlaceGenerator.MIN_LAT+ " arba didense nei "+ PlaceGenerator.MAX_LAT ;
        }
        
        if(longetude<PlaceGenerator.MIN_LON && latetude> PlaceGenerator.MAX_LON){
            errorType = "Ivesta negalima ilgumos koordinates reike. Mazesne, nei "
                    + PlaceGenerator.MIN_LON+ " arba didense nei "+ PlaceGenerator.MAX_LON ;
        }
        
        
        if (sparks < 0) {
            errorType = "Neimanomas duomuo";
        }

        return errorType;
    }

    
    @Override
    public void fromString(String places) {
        try {
            Scanner ed = new Scanner(places).useDelimiter(", ");
            this.name = ed.next();
            latetude = ed.nextDouble();
            longetude = ed.nextDouble();
            sparks = ed.nextInt();
            type = ed.next();
            validate();
        } catch (InputMismatchException e) {
            Ks.ern("Blogas duomenų formatas apie vietas -> " + places);
        } catch (NoSuchElementException e) {
            Ks.ern("Trūksta duomenų apie vietas -> " + places);
        }
    }

    
    @Override
    public String toString() {
                return String.format("%-15s %2.6f %2.6f Sparks - %d %-10s",
                name, latetude, longetude, sparks, type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatetude() {
        return latetude;
    }

    public void setLatetude(double latetude) {
        this.latetude = latetude;
    }

    public int getSparks() {
        return sparks;
    }

    public void setSparks(int sparks) {
        this.sparks = sparks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongetude() {
        return longetude;
    }

    public void setLongetude(double longetude) {
        this.longetude = longetude;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Place other = (Place) obj;
        if (this.sparks != other.sparks) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (Double.doubleToLongBits(this.latetude) != Double.doubleToLongBits(other.latetude) && 
                Double.doubleToLongBits(this.longetude) != Double.doubleToLongBits(other.longetude)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.sparks;
        hash = 89 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.latetude) ^ (Double.doubleToLongBits(this.latetude) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.longetude) ^ (Double.doubleToLongBits(this.longetude) >>> 32));
        return hash;
    }

    
    
    @Override
    public int compareTo(Place a) {
        return id.compareTo(a.id);
    }
    
    public static Comparator<Place> usingType = new Comparator<Place>() {

        @Override
        public int compare(Place p1, Place p2) {
            return p1.type.compareTo(p2.type);
        }
    };
    
    public static Comparator<Place> usingTypeAndName =
            new Comparator<Place>() {

                @Override
                public int compare(Place p1, Place p2) {
                    // pradžioje pagal tipa, o po to pagal varda
                    int cmp = p1.getType().compareTo(p2.getType());
                    if (cmp != 0) {
                        return cmp;
                    }
                    return p1.getName().compareTo(p2.getName());
                }
            };
    public static Comparator usingSparks = new Comparator() {
        // sarankiškai priderinkite prie generic interfeiso

        @Override
        public int compare(Object o1, Object o2) {
            double k1 = ((Place) o1).getSparks();
            double k2 = ((Place) o2).getSparks();
            // didėjanti tvarka, pradedant nuo mažiausios
            if (k1 < k2) {
                return -1;
            }
            if (k1 > k2) {
                return 1;
            }
            return 0;
        }
    };
    
    private double rad2deg(double rad) {
      return (rad * 180.0 / Math.PI);
    }
    private double deg2rad(double deg) {
      return (deg * Math.PI / 180.0);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
      double theta = lon1 - lon2;
      double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
      dist = Math.acos(dist);
      dist = rad2deg(dist);
      dist = dist * 60 * 1.1515;
      if (unit == 'K') {
        dist = dist * 1.609344;
      } else if (unit == 'N') {
        dist = dist * 0.8684;
        }
      return (dist);
    }
        
    public static Comparator usingNumberInStock = new Comparator() {

        @Override
        public int compare(Object o1, Object o2) {
            Place p1 = (Place) o1;
            Place p2 = (Place) o2;
            // turimas kiekis mažėjančia tvarka, esant vienodam kiekiui lyginama kaina
            double dist1 = p1.distance(p1.getLatetude(), p1.getLongetude(), CURRENT_LATETUDE, CURRENT_LONGETUDE, 'K');
            double dist2 = p2.distance(p2.getLatetude(), p2.getLongetude(), CURRENT_LATETUDE, CURRENT_LONGETUDE, 'K');
            if (dist1 < dist2) {
                return 1;
            } else if (dist1 > dist2) {
                return -1;
            } else if (p1.getSparks() < p2.getSparks()) {
                return 1;
            } else if (p1.getSparks() > p2.getSparks()) {
                return -1;
            }
            return 0;
        }
    };
}
