import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { URL_BACKEND } from '../config/config';
import { Empresa } from '../modelos/empresa';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {

  private baseEndpoint:string =URL_BACKEND+'/empresas'
  
  private cabeceras: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  public listar(): Observable<Empresa[]> {
    return this.http.get<Empresa[]>(this.baseEndpoint + '/');
  }

  public ver(id: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.baseEndpoint}/${id}`);
  }

  public guardar(empresa: Empresa): Observable<Empresa> {
    console.log ("en servicio")
    console.log(empresa)
    return this.http.post<Empresa>(this.baseEndpoint+'/', empresa, { headers: this.cabeceras });
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
