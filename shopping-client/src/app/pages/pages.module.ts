import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from './material.module';
import { PagesRoutingModule } from './pages-routing.module';
import { TopPaneComponent } from '../frmwk/top-pane/top-pane.component';
import { ProductComponent } from './product-list/product-list.component';
import { CartComponent } from './cart/cart.component';

@NgModule({
    declarations: [
        TopPaneComponent,
        ProductComponent,
        CartComponent
    ],
    imports: [
        CommonModule,
        MaterialModule,
        FormsModule,
        ReactiveFormsModule,
        PagesRoutingModule
    ],
    providers: [DatePipe]
})
export class PagesModule { }
