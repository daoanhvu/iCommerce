export class Product {
    id: number;
    name: string;
    brand: string;
    category: string;
    price: Number;
    colour: string;
}

export class CartItem {
    id: Number;
    product: Product;
    quantity: number;
}

export class Cart {
    id: Number;
    userSessionId: string;
    orderDate: Date | string;
    items: CartItem[];
}

export class UpdateCartItemRequest {
    userSessionId: string;
    cartId: Number;
    cartItemId: Number;
    quantity: Number;
}