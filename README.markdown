About Pongo
===========

Pongo is a minimal DSL that helps you to convert Java objects from one type to another.

Pongo doesn't convert objects by itself, it just organize things better.
Specifying how objects have to be converted is up to the developer.

Usage
-----
Converting an object (say x) into another (say y of type Y) is as simple as writing:

    Y y = pongo.convert(x).to(Y.class);

If you want to convert an array of X elements into an array of Y elements, this is how you do it:

    Y[] arrayOfY = pongo.convert(arrayOfX).toArrayOf(Y.class);

As of now Pongo helps you to convert:
    - arrays into arrays or lists
    - an array of (eterogeneus) objects into a single object
    - lists into lists or arrays
    - a list of (eterogeneus) objects into a single object
    - single objects into objects

When converting lists or arrays you can make use of filters like this:

    Y[] arrayOfY = pongo.convert(arrayOfX).iff(Pongo.NOT_NULL).toArrayOf(Y.class);

This will tell Pongo not to attempt to convert elements of arrayOfX that are null.

Configuration
-------------
Usuallly you will have one instance of Pongo in your project, but this is not mandatory:

    Pongo pongo = new Pongo();

The first step when using Pongo is declaring how objects should be converted.

    pongo.addConverter(new IConverter<A,B>() {
        public B convert(A a, B b, Pongo p) {
                ....
                return b;
            }

            public Class<A> getSource() {
                return A.class;
            }

            public Class<B> getTarget() {
                return B.class;
            }
    });

An instance of Pongo is passed to the convert method when conversion takes place from A to B.
This way you can chain several conversion like this:

    pongo.addConverter(new IConverter<A,B>() {
        public B convert(A a, B b, Pongo p) {
                ...
                b.setAttributeY(p.convert(a.getAttributeX()).to(Y.class));
                return b;
            }

            public Class<A> getSource() {
                return A.class;
            }

            public Class<B> getTarget() {
                return B.class;
            }
    });

Motivations
-----------
Not that I don't appreciate existing approaches (http://stackoverflow.com/questions/1432764/any-tool-for-java-object-to-object-mapping) but:

- yes writing conversion methods is very boring but it's also simple in most cases (at least most of those I run into)
- avoiding reflection can improve performance
- I wanted the compiler to clearly inform me whenever a conversion method is broken because a signature is changed
- I personally dislike XML (and people that like it in particular)
- I needed to find a readable/elegant way (at least more readable/elegant than writing tons of static methods) to declare how conversions should take place
- I wanted full control over conversion methods



