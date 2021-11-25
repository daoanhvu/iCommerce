import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { CartService } from 'src/app/services/cart.service';

@Component({
    selector: 'app-top-pane',
    templateUrl: './top-pane.component.html',
    styleUrls: ['./top-pane.component.scss']
})
export class TopPaneComponent implements OnInit {

    subscription: Subscription;
    itemCount: Number = 0;

    constructor(private cartService: CartService) {}

    ngOnInit(): void {
        this.subscription = this.cartService
            .currentMessage
            .subscribe(cart => {
                try {
                    console.log(cart);
                    this.itemCount = cart.items.length;
                } catch { }
            });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

}