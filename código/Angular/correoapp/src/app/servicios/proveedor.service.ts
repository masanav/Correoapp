import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { Proveedor } from '../modelos/proveedor';

@Injectable({
  providedIn: 'root'
})
export class ProveedorService {

  private baseEndpoint:string =URL_BACKEND+'/proveedores'
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(this.baseEndpoint+'/');
    /*
    return this.http.get<Proveedor>(this.baseEndpoint).pipe(
      map(proveedores=>proveedores as Proveedor[]).
    );
    import {map} from 'rxjs/operators';
    */
  }
/*
  public ver(id: number): Observable<Proveedor> {
    return this.http.get<Proveedor>(this.baseEndpoint + '/' + id);
  }
*/
  public verPrimero(): Observable<Proveedor> {
    return this.http.get<Proveedor>(`${this.baseEndpoint}/primero`);
  }

  public guardar(proveedor: Proveedor): Observable<Proveedor> {
    console.log(proveedor)
    return this.http.post<Proveedor>(this.baseEndpoint+'/', proveedor, { headers: this.cabeceras });
  }

  public eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseEndpoint}/${id}`);
  }
}
