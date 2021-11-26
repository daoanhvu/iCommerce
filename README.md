# iCommerce Shop
This is a simple shopping application. Because this is just a MVP so we have provided just a few functions so far
- List products 
- Add to cart
- View items in cart
- Update items in cart

To shop with this application, users don't need to create an account. A session ID will be generated, the cart information will associate with this session ID.


## Backend
Backend side has been developed using following tools and framework:
1. Java 11
2. Spring boot 2.5.5
3. Spring JPA & Hibernate
4. Maven 3.6.3

### Build the app
Checkout this repository, go to the subfolder *shopping*
mvn clean compile package

There is no data in database, I have prepared some test data with profile "h2", issue following command to see this test data:
	java -Dspring.profiles.active=h2 -jar target/shopping-0.0.1-SNAPSHOT.jar

## Web client
Web client has been developed using Angular 12.
After checkout this repository, go to the subfolder *shopping-client* and issue these commands:

yarn install
yarn build
yarn start
