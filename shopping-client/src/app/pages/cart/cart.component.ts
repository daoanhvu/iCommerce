import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';
import { Cart, Product } from 'src/app/models/product.model';
import { CartItem } from 'src/app/models/product.model';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
    selector: 'app-cart',
    templateUrl: './cart.component.html',
    styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

    cart: Cart = {
        items: [],
        userSessionId: '',
        orderDate: '2021-10-16',
        id: 0
    };
    displayedColumns: string[] = ['product', 'quantity', 'action'];

    constructor(private cartService: CartService) {}

    ngOnInit(): void {
        this.cartService.getCart()
            .subscribe(
                resp => {
                    if(resp.serviceCode == 0) {
                        this.cart = resp.result;
                    }
                },
                err => {}
            );
    }

    increaseQty(item: CartItem) {
        item.quantity = item.quantity + 1;
    }

    decreaseQty(item: CartItem) {
        if(item.quantity > 1) {
            item.quantity = item.quantity - 1;
        }
    }
}