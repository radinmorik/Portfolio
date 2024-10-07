# setupfile for the jellygame module
# use command "python setup.py sdist" to create a source distribution
# run "pip install 'filepath to .tar.gz file'" to install the module. Example: "pip install dist/JellyGameEngine-0.3.tar.gz"

from distutils.core import setup

setup(name='JellyGameEngine',
        version='0.3',
        description='jelly game engine',
        author='Jelly Game Team',
        install_requires=[
            'pyaudio >= 0.2.13',
            'wave >= 0.0.2',
            'Pillow >= 9.5.0'
        ],
        packages=['jellygame'],
        package_dir={'jellygame': '.'},
        include_package_data=True,
        classifiers=[
            'Development Status :: 3 - Alpha',
            'Intended Audience :: Developers',
            'Programming Language :: Python :: 3.10.8'
        ],
        keywords='game engine',
        python_requires='>=3.9.6'

)

