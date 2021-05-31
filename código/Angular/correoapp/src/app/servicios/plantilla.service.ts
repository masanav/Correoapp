import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { Plantilla } from '../modelos/plantilla';

@Injectable({
  providedIn: 'root'
})
export class PlantillaService {

  private baseEndpoint:string =URL_BACKEND+'/plantillas'
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Plantilla[]> {
    return this.http.get<Plantilla[]>(this.baseEndpoint + '/');
  }

  public listarPorEmpresa(id: number): Observable<Plantilla[]> {
    return this.http.get<Plantilla[]>(`${this.baseEndpoint}/empresa/${id}`);
  }

  public ver(id: number): Observable<Plantilla> {
    return this.http.get<Plantilla>(`${this.baseEndpoint}/${id}`);
  }

  public guardar(proveedor: Plantilla): Observable<Plantilla> {
    return this.http.post<Plantilla>(this.baseEndpoint+'/', proveedor, { headers: this.cabeceras });
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
}
