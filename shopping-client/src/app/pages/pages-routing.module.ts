import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CartComponent } from './cart/cart.component';
import { ProductComponent } from './product-list/product-list.component';


const routes: Routes = [
    {
        path: '',
        children: [
          {
            path: '',
            redirectTo: 'product-list',
            pathMatch: 'full'
          },
          {
            path: 'product-list',
            component: ProductComponent,
          },
          {
            path: 'my-cart',
            component: CartComponent
          }
        ]
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
