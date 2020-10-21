package models;

import lombok.*;

import static config.EnvConfig.*;
import static java.lang.Math.sqrt;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Triangle implements Comparable<Triangle> {
    @NonNull private String id;
    private Double firstSide;
    private Double secondSide;
    private Double thirdSide;

    // compare Triangle object for array sorting
    @Override
    public int compareTo(Triangle other) {

        return this.
                id.
                compareTo(other.id);
    }

    // calculate area (use Heron formula)
    public static Double calculateTriangleArea(Triangle triangle) {
        Double semipetimeter = (triangle.firstSide + triangle.secondSide + triangle.thirdSide)/2;
        return
                sqrt(semipetimeter*
                        (semipetimeter-triangle.firstSide)*
                        (semipetimeter-triangle.secondSide)*
                        (semipetimeter-triangle.thirdSide));
    }

    // calculate perimeter
    public static Double calculateTrianglePerimeter(models.Triangle triangle) {
        return triangle.firstSide + triangle.secondSide + triangle.thirdSide;
    }

    // quick generate default triangle
    public static Triangle getDefaultTriangle() {

        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                DEFAULT_TRIANGLE_FIRST_SIDE,
                DEFAULT_TRIANGLE_SECOND_SIDE,
                DEFAULT_TRIANGLE_THIRD_SIDE);
    }

    // quick generate random valid triangle
    public static Triangle getRandomTriangle() {


        double firstSideRand =
                (1-Math.random())  // zero side is is unacceptable, so we convert Random to (0,1]
                        *MAX_RANDOM_TRIANGLE_SIDE;
        double secondSideRand = (1-Math.random())*MAX_RANDOM_TRIANGLE_SIDE;

        // use triangle inequality
        double diff = Math.abs(firstSideRand-secondSideRand);
        double thirdSideRand =
                diff +
                        Math.random()*
                                (firstSideRand+secondSideRand - diff -
                                        (DELTA_FOR_SIDE *MAX_RANDOM_TRIANGLE_SIDE)); // not equal sides sum


        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                secondSideRand,
                thirdSideRand);
    }

    // quick generate random valid triangle with specified precision
    // (example: f.e. precision = 3 than sides have 3 digits after dot: 3.333;4.444;5.555)
    public static Triangle getRandomTriangle(int precision) {

        // for precision set
        double scale = Math.pow(10.0, precision);

        double firstSideRand =
                (1-Math.random())  // zero side is is unacceptable, so we convert Random to (0,1]
                        *MAX_RANDOM_TRIANGLE_SIDE;
        double secondSideRand = (1-Math.random())*MAX_RANDOM_TRIANGLE_SIDE;

        // use triangle inequality
        double diff = Math.abs(firstSideRand-secondSideRand);
        double thirdSideRand =
                diff +
                        Math.random()*
                                (firstSideRand+secondSideRand - diff -
                                        (DELTA_FOR_SIDE *MAX_RANDOM_TRIANGLE_SIDE)); // not equal sides sum


        return new Triangle(DEFAULT_TRIANGLE_ID,
                // set precision
                (Math.round(firstSideRand * scale) / scale),
                (Math.round(secondSideRand * scale) / scale),
                (Math.round(thirdSideRand * scale) / scale));

    }

    // quick generate rectangular triangle (example: 3;4;5)
    public static Triangle getRectangularTriangle() {

        double firstSideRand =
                (1 - Math.random())   // zero side is is unacceptable, so we convert Random to (0,1]
                        * MAX_RANDOM_TRIANGLE_SIDE;
        double secondSideRand = (1 - Math.random()) * MAX_RANDOM_TRIANGLE_SIDE;
        double thirdSideRand = Math.sqrt(firstSideRand*firstSideRand + secondSideRand*secondSideRand);


        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                secondSideRand,
                thirdSideRand);
    }

    // quick generate acute-angled triangle (example: 10;11;12)
    public static Triangle getAcuteAngledTriangle() {

        double firstSideRand =
                (1 - Math.random())  // zero side is is unacceptable, so we convert Random to (0,1]
                        * MAX_RANDOM_TRIANGLE_SIDE;
        double secondSideRand = (1 - Math.random()) * MAX_RANDOM_TRIANGLE_SIDE;

        double thirdSideForRectangle = Math.sqrt(firstSideRand*firstSideRand + secondSideRand*secondSideRand); // if it was rectangular triangle
        // use triangle inequality
        double diff = Math.abs(firstSideRand-secondSideRand);

        double thirdSideRand = diff +  // third side must be less than at rectangular triangle
                (1 - Math.random()) *(thirdSideForRectangle
                        - diff
                        -(DELTA_FOR_SIDE *MAX_RANDOM_TRIANGLE_SIDE)); // not equal rectangular triangle

        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                secondSideRand,
                thirdSideRand);
    }


    // quick generate obtuse triangle (example: 3;4;6)
    public static Triangle getObtuseTriangle() {

        double firstSideRand =
                (1 - Math.random())   // zero side is is unacceptable, so we convert Random to (0,1]
                        * MAX_RANDOM_TRIANGLE_SIDE;
        double secondSideRand = (1 - Math.random()) * MAX_RANDOM_TRIANGLE_SIDE;

        double thirdSideForRectangle = Math.sqrt(firstSideRand * firstSideRand + secondSideRand * secondSideRand);

        double thirdSideRand =
                thirdSideForRectangle +  // third side must be more than at rectangular triangle
                        +(Math.random() * (firstSideRand + secondSideRand - thirdSideForRectangle
                                - (DELTA_FOR_SIDE * MAX_RANDOM_TRIANGLE_SIDE))); // not equal rectangular triangle

        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                secondSideRand,
                thirdSideRand);
    }

    // quick generate isosceles triangle (example: 3;3;4)
    public static Triangle getIsoscelesTriangle() {


        double firstSideRand =
                (1-Math.random())   // zero side is is unacceptable, so we convert Random to (0,1]
                        *MAX_RANDOM_TRIANGLE_SIDE;
        double thirdSideRand = Math.random()*
                (firstSideRand + firstSideRand -  // use triangle inequality
                        (DELTA_FOR_SIDE *MAX_RANDOM_TRIANGLE_SIDE));  // not equal sides sum


        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                firstSideRand,
                thirdSideRand);
    }


    // quick generate equilateral triangle (example: 3;3;3)
    public static Triangle getEquilateralTriangle() {

        double firstSideRand =
                (1-Math.random())   // zero side is is unacceptable, so we convert Random to (0,1]
                        *MAX_RANDOM_TRIANGLE_SIDE;

        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                firstSideRand,
                firstSideRand);
    }


    // quick generate random BIG valid triangle (example: 3E10;4E10;5E10)
    public static Triangle getBigRandomTriangle() {

        double firstSideRand =
                (1-Math.random())   // zero side is is unacceptable, so we convert Random to (0,1]
                        *MAX_RANDOM_BIG_TRIANGLE_SIDE;
        double secondSideRand = (1-Math.random())*MAX_RANDOM_BIG_TRIANGLE_SIDE;

        // use triangle inequality
        double diff = Math.abs(firstSideRand-secondSideRand);
        double thirdSideRand =
                diff +
                        Math.random()*
                                (firstSideRand+secondSideRand - diff -
                                        (DELTA_FOR_SIDE *MAX_RANDOM_BIG_TRIANGLE_SIDE)); // not equal sides sum


        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                secondSideRand,
                thirdSideRand);
    }

    // quick generate random SMALL valid triangle  (example: 3E-10;4E-10;5E-10)
    public static Triangle getSmallRandomTriangle() {


        double firstSideRand =
                (1-Math.random())   // zero side is is unacceptable, so we convert Random to (0,1]
                        *MAX_RANDOM_SMALL_TRIANGLE_SIDE;
        double secondSideRand = (1-Math.random())*MAX_RANDOM_SMALL_TRIANGLE_SIDE;

        // use triangle inequality
        double diff = Math.abs(firstSideRand-secondSideRand);
        double thirdSideRand =
                diff +
                        Math.random()*
                                (firstSideRand+secondSideRand - diff -
                                        DELTA_FOR_SIDE*MAX_RANDOM_SMALL_TRIANGLE_SIDE); // not equal sides sum


        return new Triangle(
                DEFAULT_TRIANGLE_ID,
                firstSideRand,
                secondSideRand,
                thirdSideRand);
    }


}
