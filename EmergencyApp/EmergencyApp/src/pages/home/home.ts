import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { EmergencyServicePage } from '../emergency-service/emergency-service'

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})

export class HomePage
{
  numDoc:number;

  constructor(public navCtrl: NavController)
  {

  }

  ingresoApp()
  {
    this.navCtrl.push(EmergencyServicePage,
      {
        nDoc: this.numDoc
    });
  }
}
