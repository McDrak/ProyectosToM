import { Component } from '@angular/core';
import { NavController, AlertController } from 'ionic-angular';

@Component({
  selector: 'page-symptoms-page',
  templateUrl: 'symptoms-page.html'
})

export class SymptomsPagePage
{
  constructor(public navCtrl: NavController, public alertControler: AlertController) {}

  sendReport()
  {
    let alert = this.alertControler.create(
      {
        title: 'Reporte',
        subTitle: 'El reporte se ha generado exitosamente. El tiempo de espera actual en el Hospital San Ignacio es de 15 minutos.',
        buttons: ['Entendido']
      }
    );
    alert.present();
    this.navCtrl.pop();
  }
}
