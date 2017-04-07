import { Component } from '@angular/core';
import { NavController, NavParams, AlertController } from 'ionic-angular';
import { SymptomsPagePage } from '../symptoms-page/symptoms-page'
import { WaitingRoomPage } from '../waiting-room/waiting-room'

@Component({
  selector: 'page-emergency-service',
  templateUrl: 'emergency-service.html'
})

export class EmergencyServicePage
{
  nDocUsuario: number;
  public name;

  constructor(public navCtrl: NavController, public navParams: NavParams, public alertControler: AlertController)
  {
    this.nDocUsuario = navParams.get('nDoc');
    this.setNameLabel();
  }

  setNameLabel()
  {
    if(this.nDocUsuario == 1015438560)
    {
      this.name = "Alejandro";
    }
    else if(this.nDocUsuario == 1032486682)
    {
      this.name = "Felipe";
    }
    else if(this.nDocUsuario == 1019123272)
    {
      this.name = "Carlos";
    }
    else
    {
      let alert = this.alertControler.create(
        {
          title: 'Sin Registro',
          subTitle: 'No hay informacion suya en la base de datos. Porfavor revisar sus datos en su entidad medica.',
          buttons: ['Confirmar']
        }
      );
      alert.present();

      this.navCtrl.pop();
    }
  }

  setupCita()
  {
    this.navCtrl.push(SymptomsPagePage);
  }

  setupEmergencyAlert()
  {
    let alert = this.alertControler.create(
      {
        title: 'Advertencia',
        subTitle: 'El uso de este servicio es de un caso bastante dificil o de vida o muerte. Posterior a esta pantalla se procedera a llamar a una ambulancia cerca a usted.',
        buttons: [
          {
            text: 'Entiendo',
            handler: () =>
            {
              let alert2 = this.alertControler.create(
                {
                  title: 'Notificacion',
                  subTitle: 'Dentro de 10 minutos llegara una ambulancia a su recidencia.',
                  buttons: ['Confirmar']
                }
              );
              alert2.present();
            }
          },
          {
            text: 'Cancelar',
          }
        ]
      }
    );
    alert.present();
  }

  setupWaiting()
  {
    this.navCtrl.push(WaitingRoomPage);
  }
}
