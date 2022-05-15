package com.infinity.architecture.utils.permission;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

@SuppressWarnings("DuplicateBranchesInSwitch")
public class VerificaPermissoes {
    private final String TAG = "VerificaPermissao";

    // Permissões sozinhas
    public static final String PERMISSAO_LOCALIZACAO = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int PERMISSAO_LOCALIZACAO_CODIGO = 1;
    public static final String PERMISSAO_ARMAZENAMENTO_LEITURA = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int PERMISSAO_ARMAZENAMENTO_LEITURA_CODIGO = 2;
    public static final String PERMISSAO_ARMAZENAMENTO_ESCRITA = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int PERMISSAO_ARMAZENAMENTO_ESCRITA_CODIGO = 3;
    public static final String PERMISSAO_BLUETOOTH = Manifest.permission.BLUETOOTH;
    public static final int PERMISSAO_BLUETOOTH_CODIGO = 4;
    public static final String PERMISSAO_BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;
    public static final int PERMISSAO_BLUETOOTH_ADMIN_CODIGO = 5;
    public static final String PERMISSAO_INTERNET = Manifest.permission.INTERNET;
    public static final int PERMISSAO_INTERNET_CODIGO = 6;
    public static final String PERMISSAO_TELEFONE_INFO = Manifest.permission.READ_PHONE_STATE;
    public static final int PERMISSAO_TELEFONE_INFO_CODIGO = 7;
    public static final String PERMISSAO_ESTADO_WIFI = Manifest.permission.ACCESS_WIFI_STATE;
    public static final int PERMISSAO_ESTADO_WIFI_CODIGO = 8;
    public static final String PERMISSAO_ALTERAR_ESTADO_WIFI = Manifest.permission.CHANGE_WIFI_STATE;
    public static final int PERMISSAO_ALTERAR_ESTADO_WIFI_CODIGO = 9;
    public static final String PERMISSAO_REORGANIZAR_TAREFAS = Manifest.permission.REORDER_TASKS;
    public static final int PERMISSAO_REORGANIZAR_TAREFAS_CODIGO = 10;
    public static final String PERMISSAO_ALERTA_SISTEMA = Manifest.permission.SYSTEM_ALERT_WINDOW;
    public static final int PERMISSAO_ALERTA_SISTEMA_CODIGO = 11;
    public static final String PERMISSAO_ENVIAR_SMS = Manifest.permission.SEND_SMS;
    public static final int PERMISSAO_ENVIAR_SMS_CODIGO = 12;
    public static final String PERMISSAO_CONTATOS_LEITURA = Manifest.permission.READ_CONTACTS;
    public static final int PERMISSAO_CONTATOS_LEITURA_CODIGO = 13;
    public static final String PERMISSAO_CONTATOS_ESCRITA = Manifest.permission.WRITE_CONTACTS;
    public static final int PERMISSAO_CONTATOS_ESCRITA_CODIGO = 14;
    public static final String PERMISSAO_CAMERA = Manifest.permission.CAMERA;
    public static final int PERMISSAO_CAMERA_CODIGO = 15;
    public static final String PERMISSAO_NFC = Manifest.permission.NFC;
    public static final int PERMISSAO_NFC_CODIGO = 16;
    public static final String PERMISSAO_WAKELOCK = Manifest.permission.WAKE_LOCK;
    public static final int PERMISSAO_WAKELOCK_CODIGO = 17;
    public static final String PERMISSAO_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final int PERMISSAO_RECORD_AUDIO_CODIGO = 18;

    // Listener da permissao
    private InterfacePermissao interfacePermissao;

    public void verificarPermissao(Activity activity, String permissao, int codigoPermissao,
                                   InterfacePermissao interfacePermissao) {
        Log.d(TAG, "VerificaPermissao");

        if (interfacePermissao != null) {
            this.interfacePermissao = interfacePermissao;
        }

        // Verifica se a permissão foi garantida
        if (ContextCompat.checkSelfPermission(activity, permissao) != PackageManager.PERMISSION_GRANTED) { // nao permitida

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissao)) { // Requisita a permissao com explicação
                Log.d("permissionRacionale", "true - " + codigoPermissao);
                ActivityCompat.requestPermissions(activity,
                        new String[]{permissao}, codigoPermissao);

            } else { // Requisita a permissão pela primeira vez sem explicação
                Log.d("permissionRacionale", "false - " + codigoPermissao);
                ActivityCompat.requestPermissions(activity,
                        new String[]{permissao}, codigoPermissao);
            }
        } else {
            Log.d(TAG, "permissionGranted:");
            this.interfacePermissao.statusPermissao(true);
        }
    }

    // Processo a permissao escolhida
    public void onResultadoPermissao(int requestCode,
                                     @NonNull String[] permissions,
                                     @NonNull int[] grantResults) {
        Log.d(TAG, "onResultadoPermissao");

        switch (requestCode) { // Verifica as permissoes
            case VerificaPermissoes.PERMISSAO_ARMAZENAMENTO_ESCRITA_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_TELEFONE_INFO_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_ARMAZENAMENTO_LEITURA_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_BLUETOOTH_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_BLUETOOTH_ADMIN_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_INTERNET_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_ESTADO_WIFI_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_ALTERAR_ESTADO_WIFI_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_REORGANIZAR_TAREFAS_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_ALERTA_SISTEMA_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_ENVIAR_SMS_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_CONTATOS_LEITURA_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_CONTATOS_ESCRITA_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_CAMERA_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
            }
            break;

            case VerificaPermissoes.PERMISSAO_NFC_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
                break;
            }

            case VerificaPermissoes.PERMISSAO_LOCALIZACAO_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
                break;
            }

            case VerificaPermissoes.PERMISSAO_RECORD_AUDIO_CODIGO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // garantida
                    this.interfacePermissao.statusPermissao(true);
                } else { // recusada
                    this.interfacePermissao.statusPermissao(false);
                }
                break;
            }
        }
    }

    public static boolean permissionGranted(Context activity, String permissao) {
        return ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
    }
}
