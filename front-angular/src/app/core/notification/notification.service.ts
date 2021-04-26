import {Injectable, isDevMode} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {KeycloakService} from "keycloak-angular";
import * as  EventBus from 'vertx3-eventbus-client';
import {Observable, Subject} from "rxjs";
import {PaginationArgs, PaginationPage} from "../../shared/pagination.args";
import {NotificationConfig} from "./notification.config";
import {Notification, SocketResponse} from "./notification.model";
import {RxStompService} from "@stomp/ng2-stompjs";
import {StompConfig} from "@stomp/stompjs/esm6/stomp-config";


@Injectable({providedIn: 'root'})
export class NotificationService {

    private notifications$: Observable<any>;
    private subscribers: Array<any> = [];
    private subscriberIndex = 0;
    private stompConfig: StompConfig = {
        heartbeatIncoming: 0,
        heartbeatOutgoing: 10000,
        reconnectDelay: 0,
        debug: (str) => {
            console.log(str);
        }
    };

    constructor(private stompService: RxStompService,
                private http: HttpClient, private keycloakService: KeycloakService) {
        this.createObservableSocket();
        this.connect();
        this.notifications$.subscribe(value => console.log(value))
    }

    /**
     * Return an observable containing a subscribers list to the broker.
     */
    public getNotificationsObservable = () => {
        return this.notifications$;
    }

    sendWS(msg) {
        //TODO
    }

    getAll(paginationArgs: PaginationArgs): Observable<PaginationPage<Notification>> {
        let params = new HttpParams();
        params = params.set('size', `${paginationArgs.pageSize}`);
        params = params.set('page', `${paginationArgs.pageNumber}`);
        return this.http.get<PaginationPage<Notification>>(NotificationConfig.api, {params});
    }

    private createObservableSocket = () => {
        this.notifications$ = new Observable(observer => {
            const subscriberIndex = this.subscriberIndex++;
            this.addToSubscribers({index: subscriberIndex, observer});
            return () => {
                this.removeFromSubscribers(subscriberIndex);
            };
        });
    }

    private addToSubscribers = subscriber => {
        this.subscribers.push(subscriber);
    }

    private removeFromSubscribers = index => {
        for (let i = 0; i < this.subscribers.length; i++) {
            if (i === index) {
                this.subscribers.splice(i, 1);
                break;
            }
        }
    }

    /**
     * Connect and activate the client to the broker.
     */
    private connect = async () => {
        const token = await this.keycloakService.getToken();
        this.stompConfig.brokerURL = this.getBrokerURL(token);
        this.stompService.stompClient.configure(this.stompConfig);
        this.stompService.stompClient.onConnect = this.onSocketConnect;
        this.stompService.stompClient.onStompError = this.onSocketError;
        this.stompService.stompClient.onWebSocketError = this.onSocketError;
        this.stompService.stompClient.onWebSocketClose = this.onSocketError;
        this.stompService.stompClient.activate();
    }

    private getBrokerURL(token: string) {
        return (this.getHost() + NotificationConfig.ws).replace('http', 'ws') + '?access_token=' + token;
    }

    private getHost() {
        if(!isDevMode()){
            return window.location.protocol + "//" + window.location.host;
        }
        return "";
    }

    /**
     * On each connect / reconnect, we subscribe all broker clients.
     */
    private onSocketConnect = frame => {
        //this.stompService.stompClient.subscribe('/notifications', this.socketListener);
        this.stompService.stompClient.subscribe('/user/notifications', this.socketListener);
    }

    private onSocketError = errorMsg => {
        console.log('Broker reported error: ' + errorMsg);
        if (this.stompService.stompClient.active) {
            this.stompService.stompClient.deactivate();
        }
        setTimeout(() => {
            this.keycloakService.getToken()
                .then(token => {
                    this.stompConfig.brokerURL = this.getBrokerURL(token);
                    this.stompService.stompClient.configure(this.stompConfig);
                    if (!this.stompService.stompClient.active) {
                        this.stompService.stompClient.activate();
                    }
                })
        }, 2000)

    }

    private socketListener = frame => {
        this.subscribers.forEach(subscriber => {
            subscriber.observer.next(this.getMessage(frame));
        });
    }

    private getMessage = data => {
        const response: SocketResponse = {
            type: 'SUCCESS',
            message: JSON.parse(data.body)
        };
        return response;
    }
}
