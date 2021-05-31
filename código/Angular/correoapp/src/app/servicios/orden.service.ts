import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { Orden } from '../modelos/orden';

@Injectable({
  providedIn: 'root'
})
export class OrdenService {

  private baseEndpoint:string =URL_BACKEND+'/ordenes'
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Orden[]> {
    return this.http.get<Orden[]>(this.baseEndpoint + '/');
  }

  public ver(id: number): Observable<Orden> {
    return this.http.get<Orden>(`${this.baseEndpoint}/${id}`);
  }

  public guardar(Orden: Orden): Observable<any> {
    return this.http.post<Orden>(this.baseEndpoint+'/', Orden, { headers: this.cabeceras });
  }

  public listarPaginas(page: string, size: string): Observable<any>{
    const params = new HttpParams()
    .set('page', page)
    .set('size', size);
    return this.http.get<any>(`${this.baseEndpoint}/pagina`, {params: params});
  }

  public enviar(id: number): Observable<any> {
    return this.http.post<string>(`${this.baseEndpoint}/enviar/${id}`, { headers: this.cabeceras });
  }
}
