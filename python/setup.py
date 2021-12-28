from setuptools import setup, find_packages

setup(
    name = "moonapidemo",
    version = "1.0",
    keywords = ("moonapi"),
    description = "Moonapi Demo",
    long_description = "Moonapi Demo for python",

    url = "http://www.moonapi.com",
    author = "MoonapiDemo",
    author_email = "demo@moonapi.com",

    packages = find_packages(),
    include_package_data = True,
    platforms = "any",
    install_requires = [],

    scripts = [],
)