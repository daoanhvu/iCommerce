import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { SearchModel } from 'src/app/models/search-product.model';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
    selector: 'app-product-list',
    templateUrl: './product-list.component.html',
    styleUrls: ['./product-list.component.scss']
})
export class ProductComponent implements OnInit {
    products: Product[];
    displayedColumns: string[] = ['name', 'brand', 'category', 'price', 'colour', 'action'];
    searhModel: SearchModel ={
        page: 0,
        size: 25,
        query: {},
        sorts: []
    }
    totalPage: number;
    refiniments : FormGroup;
    
    constructor(private productService: ProductService, 
        private cartService: CartService,
        private formBuilder: FormBuilder,
        private dialog: MatDialog) {
        this.refiniments = this.formBuilder.group({
            name: [''],
            brand: [''],
            category: [''],
            colour: [''],
            price: this.formBuilder.group({
                from: [''],
                to: ['']
            })
        })
    }

    ngOnInit(): void {
        this.getProducts();
        this.dialog.afterAllClosed.subscribe( _ =>  this.getProducts());
    }

    getProducts() {
        for (const property of Object.keys(this.refiniments.value)) {
            if(this.refiniments.value[property] ) {
                if(!(this.refiniments.value[property] instanceof Object)) {
                    this.searhModel.query[property] = this.refiniments.value[property];
                } else {
                    this.searhModel.query[property] = {};
                }
            }
        }
        if (this.refiniments.value.price.from) {
            this.searhModel.query.price.from = Number(this.refiniments.value.price.from);
        }
        if (this.refiniments.value.price.to) {
            this.searhModel.query.price.to = Number(this.refiniments.value.price.to);
        }

        this.productService.searchProduct(this.searhModel)
            .subscribe(
                resp => {
                    this.products = resp.result.content;
                    this.totalPage = resp.result.totalPages;
                },
                err => {

                });
    }

    addToCart(prod: Product){
        console.log("Add to cart " + prod.id);
        this.cartService.addToCart(prod)
        .subscribe(
            resp => {
                if(resp.serviceCode == 0) {
                    this.cartService.getCartAsync();
                }
            },
            err => {}
        );
        // this.dialog.open(ProductDetailsComponent, {data: {productId: id}});
    }

    addProduct(){
        // this.dialog.open(ProductDetailsComponent, {data: {productId:''}});
    }

}
