# one key per input_action, can make it a list later
class InputAction:
    """
    InputAction class represents a key on the keyboard and a function to be called when pressing it
    """
    def __init__(self, window, key_string, function):
        """
        Constructor for InputAction class
        :param window: window input will be bound to
        :type window: Window
        :param key_string: string of key in form '<x>' to call the function
        :type key_string: str
        :param function: function called by the key. Must contain a event=None parameter in function definition
        :type function: function
        """
        self.window = window.tk_window
        self.key_string = key_string
        self.function = function
        self.func_id = self.window.bind(key_string, function)

    def change_function(self, new_function):
        """
        Changes the function called by the key
        :param new_function: new function called by the key
        :type new_function: function
        """
        self.function = new_function
        self.window.unbind(self.key_string, self.func_id)
        self.func_id = self.window.bind(self.key_string, new_function)

    def change_key(self, new_key_string):
        """
        Changes the key to call the function
        :param new_key_string: new string of key in form '<x>' to call the function
        :type new_key_string: str
        """
        if self.key_string is not None:
            self.window.unbind(self.key_string, self.func_id)
        self.key_string = new_key_string
        self.func_id = self.window.bind(self.key_string, self.function)

    def unbind(self):
        """
        Unbinds the InputAction. Must be called before deletion of InputAction to properly unbind it
        """
        if self.key_string is not None:
            self.window.unbind(self.key_string, self.func_id)
            self.key_string = None


# standard function called by standard input_actions
def print_input_action(input_action_name):
    print(input_action_name)


# standard input_actions
input_actions = {
    #"up": InputAction(w, "<w>", print_input_action),
    #"left": InputAction(w, "<a>", print_input_action),
    #"down": InputAction(w, "<s>", print_input_action),
    #"right": InputAction(w, "<d>", print_input_action)
}
