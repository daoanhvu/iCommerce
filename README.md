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
```mvn clean compile package```

There is no data in database, I have prepared some test data with profile "h2", issue following command to see this test data:

```java -Dspring.profiles.active=h2 -jar target/shopping-0.0.1-SNAPSHOT.jar```

## Web client
Web client has been developed using Angular 12.
After checkout this repository, go to the subfolder *shopping-client* and issue these commands:

```yarn install```
```yarn build```
```yarn start```

## User guide
After start backend and front-end application.
Open your browser and navigate to `http://<front-end-host>:9078`. You will see the product list

![Product list](https://github.com/daoanhvu/iCommerce/blob/main/docs/ProductList.png?raw=true "Product list")

At left top corner, you will see your cart information. You can click on button `Add to cart` to add product to your cart.

In the cart information pane at left top corner, click on the hyperlink *your cart* to view the cart detail.

![Cart Items](https://github.com/daoanhvu/iCommerce/blob/main/docs/CartItemList.png?raw=true "Cart Items")

Now you can click on the button `+` or `-` to increase or decrease the quantity of each item in cart.