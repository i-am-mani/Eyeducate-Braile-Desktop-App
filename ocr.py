import requests

import sys


class OCRSpaceLanguage:
    Arabic = 'ara'

    Bulgarian = 'bul'

    Chinese_Simplified = 'chs'

    Chinese_Traditional = 'cht'

    Croatian = 'hrv'

    Danish = 'dan'

    Dutch = 'dut'

    English = 'eng'

    Finnish = 'fin'

    French = 'fre'

    German = 'ger'

    Greek = 'gre'

    Hungarian = 'hun'

    Korean = 'kor'

    Italian = 'ita'

    Japanese = 'jpn'

    Norwegian = 'nor'

    Polish = 'pol'

    Portuguese = 'por'

    Russian = 'rus'

    Slovenian = 'slv'

    Spanish = 'spa'

    Swedish = 'swe'

    Turkish = 'tur'


class OCRSpace:

    def __init__(self, api_key, language='eng'):
        """ ocr.space API wrapper

        :param api_key: API key string

        :param language: document language

        """

        self.api_key = api_key

        self.language = language

        self.payload = {

            'isOverlayRequired': True,

            'apikey': self.api_key,

            'language': self.language,

        }

    def ocr_file(self, filename):
        """ OCR.space API request with local file

        :param filename: Your file path & name

        :return: Result in JSON format

        """

        with open(filename, 'rb') as f:
            r = requests.post(

                'https://api.ocr.space/parse/image',

                files={filename: f},

                data=self.payload,

            )

        return r.json()


# API :- 43be8d737088957

space = OCRSpace("43be8d737088957")

path = sys.argv[1]
# file = open("reply.txt",'w+')
lst1 = space.ocr_file(path)["ParsedResults"][0]["TextOverlay"]["Lines"]
for i in lst1:
    for j in i["Words"]:
        print(j["WordText"], end = " ")
