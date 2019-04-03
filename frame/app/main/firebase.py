class Firebase:
    def __init__(self, config):
        import pyrebase
        def noquote(s):
            return s
        self.firebase = pyrebase.initialize_app(config)
        self.database = self.firebase.database()
        self.auth = self.firebase.auth()
        pyrebase.pyrebase.quote = noquote

    def sign_in(self, email, psw):
        self.user = self.auth.sign_in_with_email_and_password(email, psw)
    
    def filter(self, key, val):
        """query the database: select url from user_photos where key = val
        Args:
            key: name of the field in a database entry 
            val: the value to search for
            user: returned value of sign_in
        Return: 
            an array of URLs to the result pictures

        eg. user = firebase.sign_in('user0@gmail.com', '123123')
            empty_name = firebase.filter('name', '',user)
        """
        query = self.database.child(self.user.get('localId')).order_by_child(key).equal_to(val).get(token = self.user.get('idToken'))
        if not query.pyres:
            return []
        all_val = query.val()
        result = []
        for val in all_val:
          result.append(self.database.child(self.user.get('localId')).child(val).get(token = self.user.get('idToken')).val().get("imageUrl"))
        return result

    def show_all(self):
        """ All pictures under the account of the given user
        Returns an array of URLs to the pictures
        """
        query = self.database.child(self.user.get('localId')).get(token = self.user.get('idToken'))
        if not query.pyres:
            return []
        all_val = query.val()
        result = []
        for val in all_val:
            result.append(self.database.child(self.user.get('localId')).child(val).get(token = self.user.get('idToken')).val().get("imageUrl"))
        return result
    