from jellygame import color as c


class Text:
    def __init__(self, text, pos_x, pos_y, span_x, span_y, text_size, text_color=c.BLACK, background_color=c.WHITE):
        """
        Constructor for Text class
        :param text: string of text to be rendered in the text box
        :type text: str
        :param pos_x: x position in the grid
        :type pos_x: int
        :param pos_y: y position in the grid
        :type pos_y: int
        :param span_x: number of tiles in the x axis
        :type span_x: int
        :param span_y: number of tiles in the y axis
        :type span_y: int
        :param text_size: size of the text
        :type text_size: int
        :param text_color: color of the text
        :type text_color: color
        :param background_color: color of the background
        :type background_color: color
        """
        self.text = text
        self.pos_x = pos_x
        self.pos_y = pos_y
        self.span_x = span_x
        self.span_y = span_y
        self.text_size = text_size
        self.text_color = text_color
        self.background_color = background_color

    def set_text(self, text):
        """
        Sets the text of the text box
        :param text: string of text to be set
        :type text: str
        """
        self.text = text

    def set_position(self, x, y):
        """
        Sets the position of the text box
        :param x: x position in the grid
        :type x: int
        :param y: y position in the grid
        :type y: int
        """
        self.pos_x = x
        self.pos_y = y

    def set_span(self, x, y):
        """
        Sets the span of the text box
        :param x: number of tiles in the x axis
        :type x: int
        :param y: number of tiles in the y axis
        :type y: int
        """
        self.span_x = x
        self.span_y = y

    def set_text_size(self, size):
        """
        Sets the text size
        :param size: new text size
        :type size: int
        """
        self.text_size = size

    def set_text_color(self, color):
        """
        Sets the text color
        :param color: color of the text
        :type color: color
        """
        self.text_color = color

    def set_background_color(self, color):
        """
        Sets the text color
        :param color: color of the background
        :type color: color
        """
        self.background_color = color
