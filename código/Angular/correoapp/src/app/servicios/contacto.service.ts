import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { Contacto } from '../modelos/contacto';

@Injectable({
  providedIn: 'root'
})
export class ContactoService {
  private baseEndpoint:string =URL_BACKEND+'/contactos'
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Contacto[]> {
    return this.http.get<Contacto[]>(this.baseEndpoint + '/');
  }

  public ver(id: number): Observable<Contacto> {
    return this.http.get<Contacto>(`${this.baseEndpoint}/${id}`);
  }

  public guardar(contacto: Contacto): Observable<Contacto> {
    return this.http.post<Contacto>(this.baseEndpoint+'/', contacto, { headers: this.cabeceras });
  }

  public eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseEndpoint}/${id}`);
  }

  public listarPaginas(page: string, size: string): Observable<any>{
    const params = new HttpParams()
    .set('page', page)
    .set('size', size);
    return this.http.get<any>(`${this.baseEndpoint}/pagina`, {params: params});
  }

  public filtrarPorNombre(termino: string): Observable<Contacto[]>{
    return this.http.get<Contacto[]>(`${this.baseEndpoint}/filtrar/${termino}`);
  }
}
