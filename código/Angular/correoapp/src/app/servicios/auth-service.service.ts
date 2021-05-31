import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { CLIENTE_CLAVE, CLIENTE_ID, URL_BACKEND } from '../config/config';
import { Rol } from '../modelos/rol';
import { Usuario } from '../modelos/usuario';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  public baseEndpoint = 'http://localhost:8090/api/security';
  private _usuario!: Usuario;
  private _token!: string;

  constructor(private http: HttpClient) { }

  public getLogged = new Subject();
  
  public get usuario(): Usuario {
    if (this._usuario != null) {
      return this._usuario;
    } else if (this._usuario == null && sessionStorage.getItem('usuario') != null) {
      this._usuario = JSON.parse(sessionStorage.getItem('usuario') || '{}') as Usuario;
      return this._usuario;
    }
    return new Usuario();
  }

  public get token(): any {
    if (this._token != null) {
      return this._token;
    } else if (this._token == null && sessionStorage.getItem('token') != null) {
      this._token = sessionStorage.getItem('token') || '{}';
      return this._token;
    }
    return null;
  }

  login(usuario: Usuario): Observable<any> {
    let urlEndpoint:string=this.baseEndpoint+ '/oauth/token';

    const credenciales = btoa(CLIENTE_ID + ':' + CLIENTE_CLAVE);

    const httpHeaders = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': 'Basic ' + credenciales
    });

    let params = new URLSearchParams();
    params.set('grant_type', 'password');
    params.set('username', usuario.username);
    params.set('password', usuario.password);
    return this.http.post<any>(urlEndpoint, params.toString(), { headers: httpHeaders });
  }

  guardarUsuario(accessToken: string): void {
    //se pueden quitar algunos como el rol, el username y la fecha
    let payload = this.obtenerDatosToken(accessToken);
    this._usuario = new Usuario();
    this._usuario.fechaRegistro = payload.fechaRegistro;
    this._usuario.username = payload.user_name;
    this._usuario.id = payload.id;
    this._usuario.enabled = payload.enabled;
    this._usuario.intentos = payload.intentos;
    this._usuario.rol.nombre = payload.authorities[0];
    //let rol: Rol = new Rol();
    //console.log(payload)
    //rol.id = payload.rol;
    //this._usuario.rol = rol;
    //console.log (this._usuario)
    sessionStorage.setItem('usuario', JSON.stringify(this._usuario));
  }

  guardarToken(accessToken: string): void {
    this._token = accessToken;
    sessionStorage.setItem('token', accessToken);
  }

  obtenerDatosToken(accessToken: string): any {
    if (accessToken != null) {
      return JSON.parse(atob(accessToken.split(".")[1]));
    }
    return null;
  }

  isAuthenticated(): boolean {
    if (this.token) {
      let payload = this.obtenerDatosToken(this.token);
      if (payload != null && payload.user_name && payload.user_name.length > 0) {
        return true;
      }
    }
    return false;
  }

  hasRole(role: string): boolean {
    if (this.usuario.rol.nombre == role) {
      return true;
    }
    return false;
  }

  logout(): void {
    this.getLogged.next("saliendo");
    this._token = null as unknown as string;
    this._usuario = new Usuario();
    sessionStorage.clear();
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('usuario');
  }

}