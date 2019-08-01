# object-storage-s3-client-api


## IBM Cloud Object Storage

### O que é?

> O IBM Cloud Object Storage é uma solução de armazenamento de objetos definido por software. Ele pode ser adquirido e implementado como uma licença somente de software para ser executado em qualquer um dos modelos de hardware certificados ou como um dispositivo totalmente configurado que pode ser implementado em um ou mais data centers.


### O que posso fazer com o IBM Cloud Object Storage?

> É possível usar o Cloud Object Storage para acessar seus dados não estruturados de qualquer lugar do mundo. É possível manter um ambiente sempre ativo e ajustar a escala de seu armazenamento para exabytes, sem qualquer interrupção. Dependendo de suas necessidades, é possível usá-lo como um repositório para IoT, dados analíticos ou dados de backup.


### Como usar o IBM Cloud Object Storage?

> É possível criar seus próprios aplicativos que usam a API de REST compatível com S3 ou usar um dos mais de 70 aplicativos certificados que podem possuir uma interface com o IBM COS. Os aplicativos oferecem uma maneira fácil de utilizar o IBM COS e incluem casos de uso, como o arquivamento de dados de um NAS ou o armazenamento de dados de backup.


### Que tipo de dados posso armazenar no IBM Cloud Object Storage?

> É possível armazenar qualquer tipo de dados, como imagens, vídeos, documentos etc., em qualquer formato.


### Quantos dados posso armazenar no IBM Cloud Object Storage?

> O volume total de dados que podem ser armazenados é praticamente ilimitado. Objetos individuais podem variar em tamanho, desde extremamente pequenos até 10 TB. A menor configuração com dispositivos IBM possui atualmente 72 TB de capacidade total e pode ser aumentada on-line, sem disrupção, até um tamanho maior que um exabyte.


### Como ele é implementado?

> O IBM Cloud Object Storage pode ser implementado em um, dois, três ou mais sites. Em cada uma das configurações, diversos dispositivos podem estar inativos e os dados ainda serão acessíveis. Se mais de um site for configurado, qualquer site poderá ler e gravar dados simultaneamente.

![alt text](http://www.ibm.com/cloud-computing/images/cloud/cleversafe-objectstorage.png)

 A IBM fornece uma API [IBM Cloud Object Storage - Java SDK](https://github.com/IBM/ibm-cos-sdk-java) para consumir os serviços Object Storage, essa API é um FORK da API da AWS [aws-sdk-java](https://github.com/aws/aws-sdk-java)

# Criando uma instância do Object Storage e suas credenciais 
* (1) No catálogo do IBM Cloud Ícone de link externo, selecione a categoria Armazenamento e clique em Object Storage. A página de configuração de serviço é aberta.

* (2) Dê à sua instância de serviço um nome ou use o nome predefinido.

* (3) Selecione o seu plano de precificação e clique em Criar. A página da instância do Object Storage é aberta.

* (4) No menu de navegação, selecione Credenciais de serviço.

* (5) Na página Credenciais de serviço, clique em Nova credencial.

* (6) Na página Incluir nova credencial, certifique-se de que a função esteja configurada como Gravador e, em seguida, clique em Incluir. A nova credencial é criada e mostrada na página Credenciais de serviço.

##### Após esses passos você obterá um json no formato abaixo.

```json{
  "apikey": "sua-api-key",
  "endpoints": "https://control.cloud-object-storage.cloud.ibm.com/v2/endpoints",
  "iam_apikey_description": "Auto-generated for key ashjcsjkcjkch",
  "iam_apikey_name": "Nome do seu serviço",
  "iam_role_crn": "crn:v1:bluemix:public:iam::::serviceRole:Writer",
  "iam_serviceid_crn": "crn:v1:bluemix:public:iam-identity::gdfsgdfgsd4543::serviceid:ServiceId-hgrgfdfd-c7c8-4c19-4terggegs",
  "resource_instance_id": "crn:v1:bluemix:public:cloud-object-storage:global:a/rgsdg4t434354:fdsfds-6dae-471b-8e60-fgrewt534t::"
}

```
## Obtendo seus endPoints.

* Para obter os endPoints de acesso da API da IBM accesse o link do campo `endpoints` do json acima.
* Esse link retornarar um json no formato abaixo.

```json
{  
   "identity-endpoints":{  
      "iam-token":"iam.cloud.ibm.com",
      "iam-policy":"iampap.cloud.ibm.com"
   },
   "service-endpoints":{  
      "cross-region":{  
         "us":{  
            "public":{  
               "us-geo":"s3.us.cloud-object-storage.appdomain.cloud",
               "Dallas":"s3.dal.us.cloud-object-storage.appdomain.cloud",
               "Washington":"s3.wdc.us.cloud-object-storage.appdomain.cloud",
               "San Jose":"s3.sjc.us.cloud-object-storage.appdomain.cloud"
            },
            "private":{  
               "us-geo":"s3.private.us.cloud-object-storage.appdomain.cloud",
               "Dallas":"s3.private.dal.us.cloud-object-storage.appdomain.cloud",
               "Washington":"s3.private.wdc.us.cloud-object-storage.appdomain.cloud",
               "San Jose":"s3.private.sjc.us.cloud-object-storage.appdomain.cloud"
            }
         },
         "ap":{  
            "public":{  
               "ap-geo":"s3.ap.cloud-object-storage.appdomain.cloud",
               "Tokyo":"s3.tok.ap.cloud-object-storage.appdomain.cloud",
               "Seoul":"s3.seo.ap.cloud-object-storage.appdomain.cloud",
               "Hong Kong":"s3.hkg.ap.cloud-object-storage.appdomain.cloud"
            },
            "private":{  
               "ap-geo":"s3.private.ap.cloud-object-storage.appdomain.cloud",
               "Tokyo":"s3.private.tok.ap.cloud-object-storage.appdomain.cloud",
               "Seoul":"s3.private.seo.ap.cloud-object-storage.appdomain.cloud",
               "Hong Kong":"s3.private.hkg.ap.cloud-object-storage.appdomain.cloud"
            }
         }
      },
      "regional":{  
         "us-south":{  
            "public":{  
               "us-south":"s3.us-south.cloud-object-storage.appdomain.cloud"
            },
            "private":{  
               "us-south":"s3.private.us-south.cloud-object-storage.appdomain.cloud"
            }
         },
         "us-east":{  
            "public":{  
               "us-east":"s3.us-east.cloud-object-storage.appdomain.cloud"
            },
            "private":{  
               "us-east":"s3.private.us-east.cloud-object-storage.appdomain.cloud"
            }
         }
      }
   }
}
```
> OBS: Esse Json não esta completo é só um exemplo.

* Observe que existe algums sub-objetos nesse json, vamos entender o que eles são.

```json
"cross-region":{ }
``` 
> Esse cara é a Região.

```json
 "us":{ }
```
> Esse cara é o local.

* Dentro do local exitem endpoints publicos e privados, Qual usar?

> 
- Os endpoints privados são para aplicativos que rodam dentro da IBM clound
- Os endpoints publicos são para aplicativos extenos 



## In progresse.......

## Reference Documentation
For further reference, please consider the following sections:

[GETTING STARTED GUIDE](https://quarkus.io/guides/getting-started-guide)

[SECURITY GUIDE](https://quarkus.io/guides/security-guide)

[IBM Cloud Object Storage - Java SDK](https://github.com/IBM/ibm-cos-sdk-java)

[aws-sdk-java](https://github.com/aws/aws-sdk-java)

[IBM Cloud Documentos/Cloud Object Storage](https://cloud.ibm.com/docs/services/cloud-object-storage/libraries?topic=cloud-object-storage-java#java-examples)

[IBM Cloud Object Storage - Introdução à API do S3](https://developer.ibm.com/recipes/tutorials/cloud-object-storage-s3-api-intro/#r_step1)
